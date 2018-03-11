package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.GExpression;
import com.squarebit.machinations.machc.runtime.components.*;
import com.squarebit.machinations.machc.runtime.expressions.TExpression;
import com.squarebit.machinations.machc.runtime.expressions.TObjectRef;
import com.squarebit.machinations.machc.runtime.instructions.Evaluate;
import com.squarebit.machinations.machc.runtime.instructions.Load;
import com.squarebit.machinations.machc.runtime.instructions.StoreField;

import java.util.*;

/**
 * Compiler for Mach machine.
 */
public final class MachCompiler {
    /**
     * Contains data when building a type.
     */
    private static class TypeBuildContext {
        private TType.Builder builder;
        private TConstructor internalConstructor;
    }

    /**
     * Contains data when compiling.
     */
    private static class CompilationContext {
        private Scope scope;
        private MethodBase method;
        private TypeBuildContext typeBuildContext;

        private Block block;
        private Stack<Block> blocks = new Stack<>();

        private Map<Object, Scope> scopes = new HashMap<>();

        /**
         * Push block.
         *
         * @param block the block
         */
        public void pushBlock(Block block) {
            blocks.push(block);
        }

        /**
         * Pop the last block.
         *
         * @return the block
         */
        public Block popBlock() {
            if (blocks.isEmpty())
                return null;
            return blocks.pop();
        }

        /**
         * Current block.
         *
         * @return the block
         */
        public Block currentBlock() {
            return blocks.peek();
        }

        /**
         * Gets scope.
         *
         * @return the scope
         */
        public Scope getScope() {
            return scope;
        }

        /**
         * Gets method.
         *
         * @return the method
         */
        public MethodBase getMethod() {
            return method;
        }

        /**
         * Push type scope.
         *
         * @param type the type
         */
        public void pushTypeScope(TType type) {
            this.scope = scopes.computeIfAbsent(type, (k) -> new TypeScope(this.scope, type));
            this.method = null;
        }

        /**
         * Push method scope.
         *
         * @param method the method
         */
        public void pushMethodScope(MethodBase method) {
            this.scope = scopes.computeIfAbsent(method, (k) -> new MethodScope(this.scope, method));
            this.method = method;
        }

        /**
         * Pop scope.
         */
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
                TType.Builder graphBuilder = new TType.Builder()
                        .setDeclaration(graph)
                        .setName(graph.getName())
                        .setBaseType(TType.GRAPH_TYPE)
                        .setImplementation(TRuntimeGraph.class);

                // Add type build context.
                TypeBuildContext context = new TypeBuildContext();
                context.builder = graphBuilder;

                // Creates internal constructor.
                context.internalConstructor =
                        graphBuilder.createConstructor().setName("__internal_ctor__").build();

                typeBuildContexts.add(context);
            }
        }

        // Field declaration.
        for (TypeBuildContext context: typeBuildContexts) {
            compilationContext.typeBuildContext = context;

            GGraph graph = context.builder.getDeclaration();

            for (GGraphField field: graph.getFields()) {
                if (field instanceof GField) {
                    context.builder.createField()
                            .setDeclaration(field)
                            .setName(field.getName())
                            .setType(TType.OBJECT_TYPE)
                            .build();
                }
            }
        }

        // Compile internal constructor.
        for (TypeBuildContext context: typeBuildContexts)
            compileInternalConstructor(context);

        typeBuildContexts.forEach(c -> types.add(c.builder.build()));

        return executable;
    }

    /**
     * Begin a method.
     * @param method
     */
    private void beginMethod(MethodBase method) {
        compilationContext.pushMethodScope(method);
        compilationContext.block = new Block();
    }

    /**
     * End a method.
     */
    private void endMethod() {
        compilationContext.method.instructions = compilationContext.block.flatten();

        compilationContext.popScope();
        compilationContext.block = null;
    }

    /**
     * Compiles the internal constructor.
     * @param context type build context
     */
    private void compileInternalConstructor(TypeBuildContext context) {
        beginMethod(context.internalConstructor);

        List<TField> fields = context.builder.getFields();

        fields.forEach(f -> buildGraphFieldInitializer(context, f));

        endMethod();
    }

    private void buildGraphFieldInitializer(TypeBuildContext context, TField field) {
        GGraphField declaration = field.getDeclaration();

        Block block = compilationContext.currentBlock();
        // Variable thisObjectRef = compilationContext.getMethod().variables.get(0);

        if (declaration instanceof GField) {
            GField fieldDeclaration = (GField)declaration;
            if (fieldDeclaration.getInitializer() == null)
                return;


            block.add(new Load(0));
            block.add(new Evaluate(compileExpression(fieldDeclaration.getInitializer())));
            block.add(new StoreField(0));
        }
    }

    private void compileAssign(TField lhs, GExpression rhs) {
        // MethodScope scope =
    }

    private TExpression compileExpression(GExpression expression) {
        return new TObjectRef(new TInteger(10));
    }
}
