package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.*;
import com.squarebit.machinations.machc.ast.statements.GReturn;
import com.squarebit.machinations.machc.avm.exceptions.CompilationException;
import com.squarebit.machinations.machc.avm.exceptions.UnknownIdentifierException;
import com.squarebit.machinations.machc.avm.expressions.*;
import com.squarebit.machinations.machc.avm.instructions.*;
import com.squarebit.machinations.machc.avm.runtime.*;
import com.squarebit.machinations.machc.avm.runtime.nodes.TPoolNode;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Compiler which compiles AST to Abstract Virtual Machine code.
 */
public final class Compiler {
    private Scope currentScope;
    private MethodInfo currentMethod;
    private TypeInfo currentType;

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
                TypeInfo typeInfo = moduleInfo.createType(graph.getName())
                        .setDeclaration(graph)
                        .setImplementingClass(TRuntimeGraph.class);
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

            if (node.getType() == GNode.Type.POOL) {
                fieldInfo.setType(CoreModule.POOL_NODE_TYPE);
            }
            else if (node.getType() == GNode.Type.TRANSITIVE) {
                fieldInfo.setType(CoreModule.TRANSITIVE_NODE_TYPE);
            }
            else if (node.getType() == GNode.Type.SOURCE) {
                fieldInfo.setType(CoreModule.SOURCE_NODE_TYPE);
            }
            else if (node.getType() == GNode.Type.DRAIN) {
                fieldInfo.setType(CoreModule.DRAIN_NODE_TYPE);
            }
            else if (node.getType() == GNode.Type.CONVERTER) {
                fieldInfo.setType(CoreModule.CONVERTER_NODE_TYPE);
            }
            else if (node.getType() == GNode.Type.END) {
                fieldInfo.setType(CoreModule.END_NODE_TYPE);
            }
            else
                throw new CompilationException("Unknown node type.");

            initializeNodeField(fieldInfo);
        }
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
            Expression size = compileExpression(block, elementDescriptor.getSize());
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
