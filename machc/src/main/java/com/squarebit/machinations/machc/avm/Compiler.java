package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.*;
import com.squarebit.machinations.machc.ast.statements.GReturn;
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
    private static class LambdaContext {
        MethodInfo containerMethod;
        InstructionBlock environmentBlock;
        InstructionBlock lambdaBlock;

        List<VariableInfo> environmentArguments = new ArrayList<>();

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

    private Scope currentScope;
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
                    GMethod methodDeclaration = methodInfo.getDeclaration();
                    this.currentMethod = methodInfo;

                    InstructionBlock block = methodInfo.getInstructionBlock();
                    for (GStatement statement: methodDeclaration.getStatements()) {
                        compileStatement(block, statement);
                    }
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
     * @param block
     * @param statement
     * @throws Exception
     */
    private void compileStatement(InstructionBlock block, GStatement statement) throws Exception {
        if (statement instanceof GReturn) {
            compileReturnStatement(block, (GReturn)statement);
        }
    }

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
        MethodInfo lambdaMethodInfo = new MethodInfo();
        LambdaTypeInfo lambdaTypeInfo = new LambdaTypeInfo().setLambdaMethod(lambdaMethodInfo);

        LambdaContext oldLambdaContext = this.currentLambdaContext;

        InstructionBlock rootBlock = lambdaMethodInfo.getInstructionBlock();
        InstructionBlock environmentBlock = new InstructionBlock().setParentScope(rootBlock);
        InstructionBlock lambdaBlock = new InstructionBlock().setParentScope(environmentBlock);

        this.currentLambdaContext = new LambdaContext()
                .setContainerMethod(this.currentMethod)
                .setEnvironmentBlock(environmentBlock)
                .setLambdaBlock(lambdaBlock);
        lambdaMethodInfo.setInstructionBlock(lambdaBlock);

        {
            VariableInfo argumentsVar = rootBlock.createTempVar();
            lambdaBlock.emit(
                    new LoadField(lambdaTypeInfo.getArgumentsField(),
                            lambdaMethodInfo.getThisVariable(),
                            argumentsVar)
            );


            {
                List<VariableInfo> variables = environmentBlock.getLocalVariables();
                for (int i = 0; i < variables.size(); i++) {
                    lambdaBlock.emit(new LoadArray(argumentsVar, new TInteger(i), variables.get(i)));
                }
            }

            {
                Expression exp = compileExpression(lambdaBlock, expression);
                VariableInfo expressionResult = lambdaBlock.createTempVar();
                lambdaBlock.emit(new Evaluate(exp, expressionResult));
                lambdaBlock.emit(new Return(expressionResult));
            }
        }

        // Create the arguments array.
        VariableInfo lengthVar = block.createTempVar();
        VariableInfo argumentsVar = block.createTempVar();

        int argumentCount = environmentBlock.getVariableCount();

        ArrayTypeInfo arrayTypeInfo = new ArrayTypeInfo().setElementType(CoreModule.OBJECT_TYPE);
        block.emit(new PutConstant(new TInteger(argumentCount), lengthVar));
        block.emit(new New(argumentsVar, arrayTypeInfo, new VariableInfo[]{ lengthVar }));

        // Now, loads the environment variables to the array.
        {
            for (int i = 0; i < environmentBlock.getLocalVariableCount(); i++) {
                VariableInfo arg = this.currentLambdaContext.environmentArguments.get(i);
                block.emit(new PutArray(arg, argumentsVar, new TInteger(i)));
            }
        }

        // Create a new TLambda with given argument array.
        VariableInfo resultVar = block.createTempVar();
        block.emit(new New(resultVar, lambdaTypeInfo, new VariableInfo[] { argumentsVar }));

        this.currentLambdaContext = oldLambdaContext;
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
                    this.currentMethod.getThisVariable(),
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
        else
            return null;
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
            FieldInfo fieldInfo = this.currentType.findField(symbolRef.getSymbolName());
            if (fieldInfo != null) {
                VariableInfo result = block.createTempVar();

                if (symbolRef.getNext() == null) {
                    block.emit(new LoadField(fieldInfo, currentMethod.getThisVariable(), result));
                    return new Variable(result);
                }
                else {
                    return null;
                }
            }
            else
                throw new UnknownIdentifierException(symbolRef.getSymbolName());
        }
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
