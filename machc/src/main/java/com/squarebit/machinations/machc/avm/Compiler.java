package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.GExpression;
import com.squarebit.machinations.machc.ast.expressions.GInteger;
import com.squarebit.machinations.machc.avm.exceptions.CompilationException;
import com.squarebit.machinations.machc.avm.expressions.Constant;
import com.squarebit.machinations.machc.avm.expressions.Expression;
import com.squarebit.machinations.machc.avm.instructions.Evaluate;
import com.squarebit.machinations.machc.avm.instructions.PutField;
import com.squarebit.machinations.machc.avm.runtime.TInteger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Compiler which compiles AST to Abstract Virtual Machine code.
 */
public final class Compiler {
    private Scope currentScope;

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
                TypeInfo typeInfo = moduleInfo.createType(graph.getName()).setDeclaration(graph);
                for (GMethod method: graph.getMethods())
                    declareMethod(typeInfo, method);
            }

            // Build fields.
            for (TypeInfo typeInfo: moduleInfo.getTypes()) {
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

        Expression expression = compileExpression(block, field.getInitializer());
        VariableInfo temp = block.createTempVar();

        block.emit(new Evaluate(expression, temp));
        block.emit(new PutField(internalInstanceConstructor.getThisVariable(), fieldInfo, temp));
    }

    private Expression compileExpression(InstructionBlock block, GExpression expression) throws Exception {
        if (expression instanceof GInteger)
            return new Constant(new TInteger(((GInteger)expression).getValue()));
        else
            throw new CompilationException("Unknown expression");
    }
}
