package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.*;
import com.squarebit.machinations.machc.ast.statements.*;
import com.squarebit.machinations.machc.avm.exceptions.CompilationException;
import com.squarebit.machinations.machc.avm.exceptions.UnknownIdentifierException;
import com.squarebit.machinations.machc.avm.expressions.*;
import com.squarebit.machinations.machc.avm.instructions.*;
import com.squarebit.machinations.machc.avm.runtime.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Compiler which compiles AST to Abstract Virtual Machine code.
 */
public final class Compiler {
    /**
     * Data for current lambda compilation.
     */
    private static class LambdaContext {
        TypeInfo currentType;
        MethodInfo containerMethod;
        InstructionBlock containerBlock;

        InstructionBlock environmentBlock;
        InstructionBlock lambdaBlock;

        List<VariableInfo> environmentArguments = new ArrayList<>();
        Map<VariableInfo, VariableInfo> bindings = new HashMap<>();

        public VariableInfo bind(VariableInfo environmentArgument) {
            return bindings.computeIfAbsent(environmentArgument, (arg) -> {
                environmentArguments.add(arg);
                return environmentBlock.createTempVar();
            });
        }

        public LambdaContext setCurrentType(TypeInfo currentType) {
            this.currentType = currentType;
            return this;
        }

        public LambdaContext setContainerBlock(InstructionBlock containerBlock) {
            this.containerBlock = containerBlock;
            return this;
        }

        public LambdaContext setContainerMethod(MethodInfo containerMethod) {
            this.containerMethod = containerMethod;
            return this;


        }

        public LambdaContext setEnvironmentBlock(InstructionBlock environmentBlock) {
            this.environmentBlock = environmentBlock;
            return this;
        }

        public LambdaContext setLambdaBlock(InstructionBlock lambdaBlock) {
            this.lambdaBlock = lambdaBlock;
            return this;
        }
    }

    private MethodInfo currentMethod;
    private TypeInfo currentType;

    private LambdaContext currentLambdaContext;

    /**
     * Compiles a unit to a module.
     *
     * @param unit the unit to compile
     * @return the {@link ModuleInfo} instance
     * @throws CompilationException if any errors occurs.
     */
    public ModuleInfo compile(GUnit unit) throws CompilationException {
        checkNotNull(unit);

        ModuleInfo moduleInfo = new ModuleInfo();

        try {
            // Create initial type and their method declarations.
            for (GGraph graph : unit.getGraphs()) {
//                TypeInfo typeInfo = moduleInfo.createType(graph.getName())
//                        .setDeclaration(graph)
//                        .setImplementingClass(TRuntimeGraph.class);

                TypeInfo typeInfo = new GraphTypeInfo();
                typeInfo.setName(graph.getName()).setDeclaration(graph);
                moduleInfo.addType(typeInfo);

                this.currentType = typeInfo;
                for (GMethod method: graph.getMethods())
                    declareMethod(typeInfo, method);

                typeInfo.reindexMethods();
            }

            // Build fields and methods.
            for (TypeInfo typeInfo: moduleInfo.getTypes()) {
                this.currentType = typeInfo;
                GGraph declaration = typeInfo.getDeclaration();

                // Fields
                for (GGraphField graphField: declaration.getFields())
                    declareField(typeInfo, graphField);
                typeInfo.reindexFields();

                // Methods
                for (MethodInfo methodInfo: typeInfo.getMethods()) {
                    compileMethod(methodInfo);
                }
            }
        }
        catch (Exception exception) {
            throw new CompilationException(exception);
        }

        // Done.
        return moduleInfo;
    }

    /**
     *
     * @param methodInfo
     * @throws Exception
     */
    private void compileMethod(MethodInfo methodInfo) throws Exception {
        GMethod methodDeclaration = methodInfo.getDeclaration();
        this.currentMethod = methodInfo;

        InstructionBlock block = methodInfo.getInstructionBlock();
        for (GStatement statement: methodDeclaration.getStatements()) {
            compileStatement(block, statement);
        }
    }

    /**
     *
     * @param block
     * @param statement
     * @throws Exception
     */
    private void compileStatement(InstructionBlock block, GStatement statement) throws Exception {
        if (statement instanceof GReturn) {
            compileReturnStatement(block, (GReturn)statement);
        }
        else if (statement instanceof GIfThenElse) {
            compileIfThenElse(block, (GIfThenElse)statement);
        }
        else if (statement instanceof GFor) {
            InstructionBlock childBlock = compileFor(block, (GFor)statement);
            block.emit(new JumpBlock(childBlock));
        }
        else if (statement instanceof GWhile) {
            compileWhileStatement(block, (GWhile)statement);
        }
        else if (statement instanceof GDoWhile) {
            compileDoWhileStatement(block, (GDoWhile)statement);
        }
        else if (statement instanceof GVariableDeclaration) {
            compileVariableDeclaration(block, (GVariableDeclaration)statement);
        }
        else if (statement instanceof GBlock) {
            InstructionBlock childBlock = compileBlock(block, (GBlock)statement);
            block.emit(new JumpBlock(childBlock));
        }
        else if (statement instanceof GExpressionStatement) {
            compileExpressionStatement(block, (GExpressionStatement)statement);
        }
        else if (statement != GStatement.EMPTY)
            throw new CompilationException("Shall not reach here");
    }

    /**
     *
     * @param block
     * @param statement
     * @throws Exception
     */
    private void compileWhileStatement(InstructionBlock block, GWhile statement) throws Exception {
        InstructionBlock whileBlock = block;

        // while-start
        Label start = new Label();
        whileBlock.emit(start);

        Expression expression = compileExpression(whileBlock, statement.getCondition());
        VariableInfo conditionVarInfo = whileBlock.createTempVar();
        whileBlock.emit(new Evaluate(new Not(expression), conditionVarInfo));

        Label end = new Label();
        whileBlock.emit(new JumpIf().setCondition(conditionVarInfo).setWhenTrue(end));

        // while-statement
        compileStatement(whileBlock, statement.getStatement());
        whileBlock.emit(new Jump(start));

        // while-end
        whileBlock.emit(end);

        whileBlock.reindexVariables();
        whileBlock.reindexInstructions();
    }

    /**
     *
     * @param block
     * @param statement
     * @throws Exception
     */
    private void compileDoWhileStatement(InstructionBlock block, GDoWhile statement) throws Exception {
        // do-while-start
        Label start = new Label();
        block.emit(start);

        // do-while-statement
        compileStatement(block, statement.getStatement());

        // end checking.
        Expression expression = compileExpression(block, statement.getCondition());
        VariableInfo conditionVarInfo = block.createTempVar();
        block.emit(new Evaluate(expression, conditionVarInfo));

        Label end = new Label();
        block.emit(new JumpIf().setCondition(conditionVarInfo).setWhenTrue(start).setWhenFalse(end));

        // do-while-end
        block.emit(end);

        block.reindexVariables();
        block.reindexInstructions();
    }

    private void compileExpressionStatement(InstructionBlock block, GExpressionStatement statement) throws Exception {
        GExpression expression = statement.getExpression();

        if (expression instanceof GAssignment) {
            compileAssignment(block, (GAssignment)expression);
        }
        else if (expression instanceof GPostfixExpression) {
            compilePostfixExpression(block, (GPostfixExpression)expression);
        }
        else
            throw new CompilationException("Shall not reach here");
    }

    private Expression compilePostfixExpression(InstructionBlock block, GPostfixExpression expression) throws Exception {
        Expression child = compileExpression(block, expression.getExpression());
        TInteger constant = expression.getOperator() == GPostfixExpression.Operator.INCREMENT ?
                new TInteger(1) : new TInteger(-1);

        if (!(child instanceof Variable))
            throw new CompilationException("Require a variable or field.");

        Variable childVar = (Variable)child;

        // The variable does reference a local variable.
        if (childVar.getData() == null) {
            if (childVar.getVariableInfo().isTemporary())
                throw new CompilationException("Require a variable or field.");

            VariableInfo preValue = block.createTempVar();
            VariableInfo postValue = block.createTempVar();

            block.emit(new Move(childVar.getVariableInfo(), preValue));

            block.emit(new Evaluate(new Add(childVar, new Constant(constant)), postValue));
            block.emit(new Move(postValue, childVar.getVariableInfo()));

            return new Variable(preValue);
        }
        else {
            if (childVar.getData() instanceof FieldAccess) {
                FieldAccess fieldAccess = (FieldAccess)childVar.getData();

                VariableInfo postValue = block.createTempVar();
                block.emit(new Evaluate(new Add(childVar, new Constant(constant)), postValue));
                block.emit(new Move(postValue, childVar.getVariableInfo()));
                block.emit(new PutField(fieldAccess.getFieldInfo(), fieldAccess.getOwner(), postValue));

                return childVar;
            }
            else
                throw new CompilationException("Should not reach here");
        }
    }

    private Expression compileAssignment(InstructionBlock block, GAssignment assignment) throws Exception {
        Expression expression = compileExpression(block, assignment.getExpression());

        if (assignment.getTarget() instanceof GSymbolRef) {
            GSymbolRef symbolRef = (GSymbolRef)assignment.getTarget();
            Symbol symbol = resolveLocalVariableOrFieldSymbol(block, symbolRef.getSymbolName());

            if (symbol == null)
                throw new CompilationException(String.format("Unknown identifier %s", symbolRef.getSymbolName()));
            else {
                if (symbol instanceof VariableInfo) {
                    VariableInfo variableInfo = (VariableInfo)symbol;

                    if (!isChildScope(block, variableInfo.getDeclaringScope()))
                        throw new CompilationException(String.format("Cannot assign to variable %s", symbolRef.getSymbolName()));

                    Variable expressionValue = emitEvaluateOrSkip(block, expression);
                    block.emit(new Move(expressionValue.getVariableInfo(), variableInfo));
                    return expressionValue;
                }
                else if (symbol instanceof FieldInfo) {
                    Variable expressionValue = emitEvaluateOrSkip(block, expression);
                    block.emit(new PutField((FieldInfo)symbol, getThisVariable(), expressionValue.getVariableInfo()));
                    return expressionValue;
                }
                else throw new CompilationException("Shall not reach here");
            }
        }
        else throw new CompilationException("Shall not reach here.");
    }

    private Variable emitEvaluateOrSkip(InstructionBlock block, Expression expression) {
        if (expression instanceof Variable) {
            return ((Variable)expression);
        }
        else {
            VariableInfo variableInfo = block.createTempVar();
            block.emit(new Evaluate(expression, variableInfo));
            return new Variable(variableInfo);
        }
    }

    /**
     * Determines if the first scope is the same or child scope of the second.
     * @param first
     * @param second
     * @return
     */
    private boolean isChildScope(Scope first, Scope second) {
        if (second == null)
            return false;

        while (first != null) {
            if (first == second)
                return true;
            first = first.getParentScope();
        }

        return false;
    }

    private void compileFlatBlockOrSingleStatement(InstructionBlock block, GStatement statement) throws Exception {
        if (statement instanceof GBlock) {
            GBlock statementBlock = (GBlock)statement;
            for (GStatement s: statementBlock.getStatements())
                compileStatement(block, s);
        }
        else
            compileStatement(block, statement);
    }

    private Symbol resolveLocalVariableOrFieldSymbol(Scope scope, String name) throws Exception {
        // Try to find in current scope first.
        VariableInfo localVar = scope.findVariable(name);

        if (localVar != null)
            return localVar;
        else {
            // Try to find in lambda container scope
            if (this.currentLambdaContext != null)
                localVar = this.currentLambdaContext.containerBlock.findVariable(name);

            if (localVar != null)
                return localVar;
            else {
                TypeInfo currentType = this.currentLambdaContext == null ?
                        this.currentType : this.currentLambdaContext.currentType;

                return currentType.findField(name);
            }
        }
    }

    private InstructionBlock compileFor(InstructionBlock block, GFor instruction) throws Exception {
        InstructionBlock forBlock = new InstructionBlock().setParentScope(block);

        // for-init
        compileFlatBlockOrSingleStatement(forBlock, instruction.getInit());

        // for-condition check.
        Label start = new Label();
        forBlock.emit(start);

        // Evaluate the "not-condition".
        VariableInfo conditionVar = forBlock.createTempVar();
        Expression condition = compileExpression(forBlock, instruction.getExpression());
        forBlock.emit(new Evaluate(new Not(condition), conditionVar));

        Label end = new Label();
        forBlock.emit(new JumpIf().setCondition(conditionVar).setWhenTrue(end));

        // for-body
        compileFlatBlockOrSingleStatement(forBlock, instruction.getStatement());

        // for-update
        compileFlatBlockOrSingleStatement(forBlock, instruction.getUpdate());

        //
        forBlock.emit(new Jump(start));

        // for-end
        forBlock.emit(end);

        forBlock.reindexVariables();
        forBlock.reindexInstructions();
        return forBlock;
    }

    private InstructionBlock compileBlock(InstructionBlock block, GBlock statement) throws Exception {
        List<GStatement> statements = statement.getStatements();

        InstructionBlock thisBlock = new InstructionBlock().setParentScope(block);

        for (int i = 0; i < statements.size(); i++) {
            compileStatement(thisBlock, statements.get(i));
        }

        return thisBlock;
    }

    /**
     *
     * @param block
     * @param declaration
     * @throws Exception
     */
    private void compileVariableDeclaration(InstructionBlock block, GVariableDeclaration declaration) throws Exception {
        List<GVariableDeclarator> declarators = declaration.getDeclarators();

        for (int i = 0; i < declarators.size(); i++) {
            GVariableDeclarator declarator = declarators.get(i);
            VariableInfo variable = block.createVariable(declarator.getName());
            if (declarator.getInitializer() != null) {
                Expression expression = compileExpression(block, declarator.getInitializer());
                block.emit(new Evaluate(expression, variable));
            }
        }
    }

    /**
     *
     * @param block
     * @param statement
     * @throws Exception
     */
    private void compileIfThenElse(InstructionBlock block, GIfThenElse statement) throws Exception {
        VariableInfo conditionVar = block.createTempVar();

        // Condition.
        Expression condition = compileExpression(block, statement.getCondition());
        block.emit(new Evaluate(condition, conditionVar));

        InstructionBlock whenTrue = null;
        if (statement.getWhenTrue() instanceof GBlock) {
            whenTrue = compileBlock(block, (GBlock)statement.getWhenTrue());
        }
        else {
            whenTrue = new InstructionBlock().setParentScope(block);
            compileStatement(whenTrue, statement.getWhenTrue());
        }

        InstructionBlock whenFalse = null;
        if (statement.getWhenFalse() != null) {
            if (statement.getWhenFalse() instanceof GBlock) {
                whenFalse = compileBlock(block, (GBlock)statement.getWhenFalse());
            }
            else {
                whenFalse = new InstructionBlock().setParentScope(block);
                compileStatement(whenFalse, statement.getWhenFalse());
            }
        }

        block.emit(new JumpBlockIf(conditionVar, whenTrue, whenFalse));
    }

    /**
     *
     * @param block
     * @param statement
     * @throws Exception
     */
    private void compileReturnStatement(InstructionBlock block, GReturn statement) throws Exception {
        VariableInfo resultVar = block.createTempVar();
        Expression resultExpression = compileExpression(block, statement.getExpression());

        block.emit(new Evaluate(resultExpression, resultVar));
        block.emit(new Return(resultVar));
    }

    /**
     * Declare a method.
     * @param typeInfo the type containing the method.
     * @param method method
     * @throws CompilationException if any errors occur.
     */
    private void declareMethod(TypeInfo typeInfo, GMethod method) throws Exception {
        MethodInfo methodInfo = typeInfo.createMethod(method.getName()).setDeclaration(method);

        // TODO: Adding parameters
        List<String> arguments = method.getArguments();
        for (int i = 0; i < arguments.size(); i++) {
            ParameterInfo parameterInfo = methodInfo.createParameter(arguments.get(i));
            parameterInfo.setType(CoreModule.OBJECT_TYPE);
        }
    }

    /**
     * Declares a field.
     * @param typeInfo type containing the field.
     * @param graphField field instance
     * @throws Exception if any errors occur.
     */
    private void declareField(TypeInfo typeInfo, GGraphField graphField) throws Exception {
        FieldInfo fieldInfo = typeInfo.createField(graphField.getName())
                .setDeclaration(graphField);

        if (graphField instanceof GField) {
            GField field = (GField)graphField;
            fieldInfo.setType(CoreModule.OBJECT_TYPE);

            if (field.getInitializer() != null)
                initializeField(fieldInfo);
        }
        else if (graphField instanceof GNode) {
            GNode node = (GNode)graphField;
            GNodeType nodeType = node.getType();

            if (nodeType.isBuiltin()) {
                if (nodeType.getBuiltinType() == GNode.Type.POOL) {
                    fieldInfo.setType(CoreModule.POOL_NODE_TYPE);
                }
                else if (nodeType.getBuiltinType() == GNode.Type.TRANSITIVE) {
                    fieldInfo.setType(CoreModule.TRANSITIVE_NODE_TYPE);
                }
                else if (nodeType.getBuiltinType() == GNode.Type.SOURCE) {
                    fieldInfo.setType(CoreModule.SOURCE_NODE_TYPE);
                }
                else if (nodeType.getBuiltinType() == GNode.Type.DRAIN) {
                    fieldInfo.setType(CoreModule.DRAIN_NODE_TYPE);
                }
                else if (nodeType.getBuiltinType() == GNode.Type.CONVERTER) {
                    fieldInfo.setType(CoreModule.CONVERTER_NODE_TYPE);
                }
                else if (nodeType.getBuiltinType() == GNode.Type.END) {
                    fieldInfo.setType(CoreModule.END_NODE_TYPE);
                }
                else
                    throw new CompilationException("Unknown node type.");
            }
            else {
                TypeInfo graphType = typeInfo.getModule().findType(nodeType.getTypeName());
                if (graphType == null) {
                    throw  new CompilationException("Unknown graph type");
                }
                else {
                    GraphNodeTypeInfo graphNodeTypeInfo = new GraphNodeTypeInfo();
                    graphNodeTypeInfo.setGraphType(graphType);

                    fieldInfo.setType(graphNodeTypeInfo);
                }
            }

            initializeNodeField(fieldInfo);
        }
        else if (graphField instanceof GConnection) {
            fieldInfo.setType(CoreModule.CONNECTION_TYPE);
            initializeConnectionField(fieldInfo);
        }
    }

    /**
     *
     * @param fieldInfo
     * @throws Exception
     */
    private void initializeConnectionField(FieldInfo fieldInfo) throws Exception {
        checkArgument(fieldInfo.getDeclaration() instanceof GConnection);

        TypeInfo graphType = fieldInfo.getDeclaringType();

        MethodInfo internalInstanceConstructor = fieldInfo.getDeclaringType().getInternalInstanceConstructor();
        InstructionBlock block = internalInstanceConstructor.getInstructionBlock();
        this.currentMethod = internalInstanceConstructor;

        GConnection connection = (GConnection)fieldInfo.getDeclaration();
        GConnectionDescriptor descriptor = connection.getDescriptor();

        VariableInfo[] args = new VariableInfo[3];

        if (descriptor.getFrom() != null) {
            FieldInfo fromField = graphType.findField(descriptor.getFrom());
            if (fromField != null) {
                VariableInfo fromVar = block.createTempVar();
                block.emit(new LoadField(fromField, internalInstanceConstructor.getThisVariable(), fromVar));
                args[0] = fromVar;
            }
        }

        if (descriptor.getTo() != null) {
            FieldInfo toField = graphType.findField(descriptor.getTo());
            if (toField != null) {
                VariableInfo toVar = block.createTempVar();
                block.emit(new LoadField(toField, internalInstanceConstructor.getThisVariable(), toVar));
                args[1] = toVar;
            }
        }

        if (descriptor.getFlow() != null) {
            SetDescriptor flow = compileSetDescriptor(block, descriptor.getFlow());
            VariableInfo flowVar = block.createTempVar();
            block.emit(new PutConstant(new TExpression(flow), flowVar));
            args[2] = flowVar;
        }

        VariableInfo instanceVar = block.createTempVar();
        block.emit(new New(instanceVar, CoreModule.CONNECTION_TYPE, args));
        block.emit(new PutField(fieldInfo, internalInstanceConstructor.getThisVariable(), instanceVar));
    }

    /**
     *
     * @param fieldInfo
     * @throws Exception
     */
    private void initializeNodeField(FieldInfo fieldInfo) throws Exception {
        checkArgument(fieldInfo.getDeclaration() instanceof GNode);

        GNode node = (GNode)fieldInfo.getDeclaration();

        MethodInfo internalInstanceConstructor = fieldInfo.getDeclaringType().getInternalInstanceConstructor();
        InstructionBlock block = internalInstanceConstructor.getInstructionBlock();

        this.currentMethod = internalInstanceConstructor;

        VariableInfo fieldValue = block.createTempVar();

        if (node.getInitializer() != null) {
            SetDescriptor descriptor = compileSetDescriptor(block, node.getInitializer());
            Set set = new Set(descriptor);

            VariableInfo temp = block.createTempVar();

            block.emit(new Evaluate(set, temp));
            block.emit(new New(fieldValue, fieldInfo.getType(), new VariableInfo[]{ temp }));
        }
        else {
            block.emit(new New(fieldValue, fieldInfo.getType()));
        }

        block.emit(new PutField(fieldInfo, internalInstanceConstructor.getThisVariable(), fieldValue));
    }

    /**
     * Initializes a {@link FieldInfo} corresponding to a {@link GField}.
     * @param fieldInfo the field to initialize
     * @throws Exception if any errors occur.
     */
    private void initializeField(FieldInfo fieldInfo) throws Exception {
        checkArgument(fieldInfo.getDeclaration() instanceof GField);

        GField field = (GField)fieldInfo.getDeclaration();
        if (field.getInitializer() == null)
            return;

        MethodInfo internalInstanceConstructor = fieldInfo.getDeclaringType().getInternalInstanceConstructor();
        InstructionBlock block = internalInstanceConstructor.getInstructionBlock();

        this.currentMethod = internalInstanceConstructor;

        Expression expression = compileExpression(block, field.getInitializer());
        VariableInfo temp = block.createTempVar();

        block.emit(new Evaluate(expression, temp));
        block.emit(new PutField(fieldInfo, internalInstanceConstructor.getThisVariable(), temp));
    }

    /**
     * Compiles an expression to a lambda expression.
     * @param block
     * @param expression
     * @return
     * @throws Exception
     */
    private Variable compileLambdaExpression(InstructionBlock block, GExpression expression) throws Exception {
        // Save context
        LambdaContext previousLambdaContext = this.currentLambdaContext;
        MethodInfo previousMethod = this.currentMethod;
        TypeInfo previousType = this.currentType;

        MethodInfo lambdaMethodInfo = new MethodInfo();
        LambdaTypeInfo lambdaTypeInfo = new LambdaTypeInfo().setLambdaMethod(lambdaMethodInfo);

        InstructionBlock rootBlock = lambdaMethodInfo.getInstructionBlock();
        InstructionBlock environmentBlock = new InstructionBlock().setParentScope(rootBlock);
        InstructionBlock lambdaBlock = new InstructionBlock().setParentScope(environmentBlock);

        this.currentLambdaContext = new LambdaContext()
                .setCurrentType(this.currentType)
                .setContainerMethod(this.currentMethod)
                .setContainerBlock(block)
                .setEnvironmentBlock(environmentBlock)
                .setLambdaBlock(lambdaBlock);

        this.currentType = lambdaTypeInfo;
        this.currentMethod = lambdaMethodInfo;

        {
            Expression exp = compileExpression(lambdaBlock, expression);

            VariableInfo argumentsVar = rootBlock.createTempVar();
            environmentBlock.emit(
                    new LoadField(lambdaTypeInfo.getArgumentsField(),
                            lambdaMethodInfo.getThisVariable(),
                            argumentsVar)
            );

            {
                List<VariableInfo> variables = environmentBlock.getLocalVariables();
                for (int i = 0; i < variables.size(); i++) {
                    environmentBlock.emit(new LoadArray(argumentsVar, new TInteger(i), variables.get(i)));
                }
            }

            {
                VariableInfo expressionResult = lambdaBlock.createTempVar();
                lambdaBlock.emit(new Evaluate(exp, expressionResult));
                lambdaBlock.emit(new Return(expressionResult));
            }

            rootBlock.reindexVariables();
            environmentBlock.reindexVariables();
            lambdaBlock.reindexVariables();

            rootBlock.emit(new JumpBlock(environmentBlock));
            environmentBlock.emit(new JumpBlock(lambdaBlock));

        }

        // Create the arguments array.
        VariableInfo lengthVar = block.createTempVar();
        VariableInfo argumentsVar = block.createTempVar();

        int argumentCount = environmentBlock.getLocalVariableCount();

        ArrayTypeInfo arrayTypeInfo = new ArrayTypeInfo().setElementType(CoreModule.OBJECT_TYPE);
        block.emit(new PutConstant(new TInteger(argumentCount), lengthVar));
        block.emit(new New(argumentsVar, arrayTypeInfo, new VariableInfo[]{ lengthVar }));

        // Now, loads the environment variables to the array.
        {
            for (int i = 0; i < argumentCount; i++) {
                VariableInfo arg = this.currentLambdaContext.environmentArguments.get(i);
                block.emit(new PutArray(arg, argumentsVar, new TInteger(i)));
            }
        }

        // Create a new TLambda with given argument array.
        VariableInfo resultVar = block.createTempVar().setType(lambdaTypeInfo);
        block.emit(new New(resultVar, lambdaTypeInfo, new VariableInfo[] { argumentsVar }));

        // restore
        this.currentLambdaContext = previousLambdaContext;
        this.currentMethod = previousMethod;
        this.currentType = previousType;

        return new Variable(resultVar);
    }

    /**
     * Compile an ast expression to machine executable expression.
     * @param block the block in which the expression will be evaluated
     * @param expression the AST expression
     * @return machine executable {@link Expression}
     * @throws Exception any error occur
     */
    private Expression compileExpression(InstructionBlock block, GExpression expression) throws Exception {
        if (expression instanceof GInteger)
            return new Constant(new TInteger(((GInteger)expression).getValue()));
        else if (expression instanceof GFloat)
            return new Constant(new TFloat(((GFloat)expression).getValue()));
        else if (expression instanceof GRandomDice) {
            GRandomDice randomDice = (GRandomDice)expression;
            return new Constant(new TRandomDice(randomDice.getTimes(), randomDice.getFaces()));
        }
        else if (expression instanceof GBoolean) {
            GBoolean value = (GBoolean)expression;
            return new Constant(TBoolean.from(value == GBoolean.TRUE));
        }
        else if (expression instanceof GString) {
            GString value = (GString)expression;
            return new Constant(new TString(value.getValue()));
        }
        else if (expression instanceof GSetDescriptor) {
            return compileSetDescriptor(block, (GSetDescriptor)expression);
        }
        else if (expression instanceof GBinaryExpression) {
            return compileBinaryExpression(block, (GBinaryExpression)expression);
        }
        else if (expression instanceof GSymbolRef) {
            return compileSymbolRefExpression(block, (GSymbolRef)expression);
        }
        else if (expression instanceof GMethodInvocation) {
            return compileMethodInvocation(block, (GMethodInvocation)expression);
        }
        else
            throw new CompilationException("Unknown expression");
    }

    private SetDescriptor compileSetDescriptor(InstructionBlock block, GSetDescriptor setDescriptor) throws Exception {
        SetDescriptor result = new SetDescriptor();

        for (GSetElementDescriptor elementDescriptor : setDescriptor.getElementDescriptors()) {
            Variable size = compileLambdaExpression(block, elementDescriptor.getSize());
            Constant capacity = elementDescriptor.getCapacity() != null ?
                    new Constant(new TInteger((elementDescriptor.getCapacity()).getValue())) : null;
            Constant name = elementDescriptor.getName() != null ?
                    new Constant(new TString((elementDescriptor.getName()).getValue())) : null;

            result.add(new SetElementDescriptor(size, capacity, name));
        }

        // block.emit(new New(temp, CoreModule.SET_DESCRIPTOR_TYPE));
        return result;
    }

    private Variable compileMethodInvocation(InstructionBlock block, GMethodInvocation invocation) throws Exception
    {
        if (invocation.getReference() == GThis.INSTANCE) {
            MethodInfo methodInfo = this.currentType.findMethod(invocation.getMethodName());

            if (methodInfo == null)
                throw new RuntimeException("Method not found");

            int parameterCount = methodInfo.getParameters().size();
            VariableInfo[] parameterVariables = new VariableInfo[parameterCount];

            for (int i = 0; i < parameterCount; i++) {
                parameterVariables[i] = block.createTempVar();

                Expression expression = compileExpression(block, invocation.getArguments()[i]);
                block.emit(new Evaluate(expression, parameterVariables[i]));
            }

            VariableInfo result = block.createTempVar();
            block.emit(new Invoke(
                    methodInfo,
                    getThisVariable(),
                    parameterVariables,
                    result
            ));

            return new Variable(result);
        }
        else
            throw new RuntimeException("Not implemented");
    }

    /**
     *
     * @param block
     * @param binaryExpression
     * @return
     * @throws Exception
     */
    private Expression compileBinaryExpression(InstructionBlock block, GBinaryExpression binaryExpression)
            throws Exception
    {
        Expression first = compileExpression(block, binaryExpression.getFirst());
        Expression second = compileExpression(block, binaryExpression.getSecond());

        if (binaryExpression.getOperator().equals("+")) {
            return new Add(first, second);
        }
        else if (binaryExpression.getOperator().equals("-")) {
            return new Subtract(first, second);
        }
        else if (binaryExpression.getOperator().equals("<")) {
            return new Compare(Compare.Operator.parse(binaryExpression.getOperator()), first, second);
        }
        else if (binaryExpression.getOperator().equals("<=")) {
            return new Compare(Compare.Operator.parse(binaryExpression.getOperator()), first, second);
        }
        else if (binaryExpression.getOperator().equals(">")) {
            return new Compare(Compare.Operator.parse(binaryExpression.getOperator()), first, second);
        }
        else if (binaryExpression.getOperator().equals(">=")) {
            return new Compare(Compare.Operator.parse(binaryExpression.getOperator()), first, second);
        }
        else if (binaryExpression.getOperator().equals("==")) {
            return new Compare(Compare.Operator.parse(binaryExpression.getOperator()), first, second);
        }
        else if (binaryExpression.getOperator().equals("!=")) {
            return new Compare(Compare.Operator.parse(binaryExpression.getOperator()), first, second);
        }
        else
            throw new CompilationException("Shall not reach here");
    }

    /**
     *
     * @param block
     * @param symbolRef
     * @return
     * @throws Exception
     */
    private Expression compileSymbolRefExpression(InstructionBlock block, GSymbolRef symbolRef) throws Exception {
        VariableInfo localVar = block.findVariable(symbolRef.getSymbolName());

        TypeInfo currentType = this.currentLambdaContext == null ? this.currentType : this.currentLambdaContext.currentType;

        if (localVar != null) {
            if (symbolRef.getNext() == null)
                return new Variable(localVar);
            else {
                VariableInfo result = block.createTempVar();
                compileFieldRefRecurisve(block, symbolRef.getNext(), localVar, result);
                return new Variable(result);
            }
        }
        else {
            FieldInfo fieldInfo = currentType.findField(symbolRef.getSymbolName());
            if (fieldInfo != null) {
                VariableInfo result = block.createTempVar();

                if (symbolRef.getNext() == null) {
                    VariableInfo thisVariable = getThisVariable();

                    block.emit(new LoadField(fieldInfo, thisVariable, result));
                    return new Variable(result).setData(new FieldAccess().setFieldInfo(fieldInfo).setOwner(thisVariable));
                }
                else {
                    return null;
                }
            }
            else
                throw new UnknownIdentifierException(symbolRef.getSymbolName());
        }
    }

    private VariableInfo getThisVariable() {
        if (this.currentLambdaContext != null) {
            return currentLambdaContext.bind(this.currentLambdaContext.containerMethod.getThisVariable());
        }
        else
            return this.currentMethod.getThisVariable();
    }

    private Expression compileFieldRefRecurisve(InstructionBlock block, GSymbolRef ref, VariableInfo fieldOwner, VariableInfo result)
            throws Exception
    {
        TypeInfo fieldOnwerType = fieldOwner.getType();
        FieldInfo fieldInfo = fieldOnwerType.findField(ref.getSymbolName());

        if (fieldInfo != null) {
            if (ref.getNext() == null) {
                block.emit(new LoadField(fieldInfo, fieldOwner, result));
            }
            else {
                VariableInfo temp = block.createTempVar();
                block.emit(new LoadField(fieldInfo, fieldOwner, temp));
                compileFieldRefRecurisve(block, ref.getNext(), temp, result);
            }
        }
        else
            throw new RuntimeException("dsfdsds");

        return null;
    }
}
