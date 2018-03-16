package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.*;
import com.squarebit.machinations.machc.avm.exceptions.CompilationException;
import com.squarebit.machinations.machc.avm.exceptions.UnknownIdentifierException;
import com.squarebit.machinations.machc.avm.expressions.Add;
import com.squarebit.machinations.machc.avm.expressions.Constant;
import com.squarebit.machinations.machc.avm.expressions.Expression;
import com.squarebit.machinations.machc.avm.expressions.Variable;
import com.squarebit.machinations.machc.avm.instructions.Evaluate;
import com.squarebit.machinations.machc.avm.instructions.LoadField;
import com.squarebit.machinations.machc.avm.instructions.PutField;
import com.squarebit.machinations.machc.avm.runtime.*;

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
            }

            // Build fields.
            for (TypeInfo typeInfo: moduleInfo.getTypes()) {
                this.currentType = typeInfo;
                GGraph declaration = typeInfo.getDeclaration();
                for (GGraphField graphField: declaration.getFields())
                    declareField(typeInfo, graphField);

                typeInfo.reindex();
            }
        }
        catch (Exception exception) {
            throw new CompilationException(exception);
        }

        // Done.
        return moduleInfo;
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
        block.emit(new PutField(internalInstanceConstructor.getThisVariable(), fieldInfo, temp));
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
        else if (expression instanceof GBinaryExpression) {
            return compileBinaryExpression(block, (GBinaryExpression)expression);
        }
        else if (expression instanceof GSymbolRef) {
            return compileSymbolRefExpression(block, (GSymbolRef)expression);
        }
        else
            throw new CompilationException("Unknown expression");
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
                    block.emit(new LoadField(currentMethod.getThisVariable(), fieldInfo, result));
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
                block.emit(new LoadField(fieldOwner, fieldInfo, result));
            }
            else {
                VariableInfo temp = block.createTempVar();
                block.emit(new LoadField(fieldOwner, fieldInfo, temp));
                compileFieldRefRecurisve(block, ref.getNext(), temp, result);
            }
        }
        else
            throw new RuntimeException("dsfdsds");
    }
}
