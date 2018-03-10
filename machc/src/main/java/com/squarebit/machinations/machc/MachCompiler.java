package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.GExpression;
import com.squarebit.machinations.machc.runtime.components.*;
import com.squarebit.machinations.machc.runtime.expressions.TExpression;

import java.util.*;

/**
 * Compiler for Mach machine.
 */
public final class MachCompiler {
    private static class TypeBuildContext {
        private TType.Builder builder;
        private TConstructor internalConstructor;
    }

    private static class CompilationContext {
        private Scope scope;
        private MethodBase method;

        private Map<Object, Scope> scopes = new HashMap<>();

        public Scope getScope() {
            return scope;
        }

        public MethodBase getMethod() {
            return method;
        }

        public void pushTypeScope(TType type) {
            this.scope = scopes.computeIfAbsent(type, (k) -> new TypeScope(this.scope, type));
            this.method = null;
        }

        public void pushMethodScope(MethodBase method) {
            this.scope = scopes.computeIfAbsent(method, (k) -> new MethodScope(this.scope, method));
            this.method = method;
        }

        public void popScope() {
            if (scope != null) {
                scope = scope.getParent();

                if (scope != null) {
                    this.method = (scope instanceof MethodScope) ? ((MethodScope)scope).getMethod() : null;
                }
                else
                    this.method = null;
            }
        }
    }

    private List<TypeBuildContext> typeBuildContexts = new ArrayList<>();
    private CompilationContext compilationContext = new CompilationContext();
    private List<TType> types = new ArrayList<>();

    /**
     * Instantiates a new Mach compiler.
     */
    private MachCompiler() {
    }

    /**
     * Compile a program declaration to a mach executable.
     *
     * @param program the program
     * @return the mach executable
     * @throws Exception the exception
     */
    public static MachExecutable compile(GProgram program) {
        return new MachCompiler().doCompile(program);
    }

    /**
     * Performs compilation.
     * @param program the program to compile
     * @return a mach executable
     */
    private MachExecutable doCompile(GProgram program) {
        MachExecutable executable = new MachExecutable();

        // Type declaration.
        for (GUnit unit : program.getUnits()) {
            for (GGraph graph : unit.getGraphs()) {
                TType.Builder<TRuntimeGraph> graphBuilder = new TType.Builder<TRuntimeGraph>()
                        .setDeclaration(graph)
                        .setName(graph.getName())
                        .setBaseType(TType.GRAPH_TYPE)
                        .setImplementation(TRuntimeGraph.class);

                // Add type build context.
                TypeBuildContext context = new TypeBuildContext();
                context.builder = graphBuilder;
                context.internalConstructor = buildInternalConstructor(graphBuilder);

                typeBuildContexts.add(context);
            }
        }

        // Field declaration.
        for (TypeBuildContext context: typeBuildContexts) {
            GGraph graph = context.builder.getDeclaration();

            compilationContext.pushTypeScope(context.builder.getTarget());

            for (GGraphField field: graph.getFields()) {
                if (field instanceof GField) {
                    context.builder.createField()
                            .setDeclaration(field)
                            .setName(field.getName())
                            .setType(TType.OBJECT_TYPE)
                            .build();
                }
            }

            compilationContext.popScope();
        }

        // Compile field initializers.
        for (TypeBuildContext context: typeBuildContexts) {
            compilationContext.pushTypeScope(context.builder.getTarget());
            List<TField> fields = context.builder.getFields();

            fields.forEach(f -> buildGraphFieldInitializer(context, f));

            compilationContext.popScope();
        }

        typeBuildContexts.forEach(c -> types.add(c.builder.build()));

        return executable;
    }

    /**
     * Adds a type to known types.
     * @param type type.
     */
    private void addType(TType type) {

        this.types.add(type);
    }

    /**
     * Build the internal constructor.
     * @param graphBuilder graph builder.
     */
    private TConstructor buildInternalConstructor(TType.Builder graphBuilder) {
        return graphBuilder.createConstructor()
                .setName("__internal_ctor__")
                .build();
    }

    private void buildGraphFieldInitializer(TypeBuildContext context, TField field) {
        GGraphField declaration = field.getDeclaration();

        compilationContext.pushMethodScope(context.internalConstructor);

        if (declaration instanceof GField) {
            GField fieldDeclaration = (GField)declaration;
            if (fieldDeclaration.getInitializer() == null)
                return;


            compileExpression(fieldDeclaration.getInitializer());
        }

        compilationContext.popScope();
    }

    private void compileAssign(TField lhs, GExpression rhs) {
        // MethodScope scope =
    }

    private TExpression compileExpression(GExpression expression) {
        return null;
    }
}
