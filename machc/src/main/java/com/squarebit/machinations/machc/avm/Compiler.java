package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.ast.GGraph;
import com.squarebit.machinations.machc.ast.GGraphField;
import com.squarebit.machinations.machc.ast.GMethod;
import com.squarebit.machinations.machc.ast.GUnit;
import com.squarebit.machinations.machc.avm.exceptions.CompilationException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Compiler which compiles AST to Abstract Virtual Machine code.
 */
public final class Compiler {
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
        FieldInfo fieldInfo = typeInfo.createField(graphField.getName()).setDeclaration(graphField);
    }
}
