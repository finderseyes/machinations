package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.GExpression;
import com.squarebit.machinations.machc.runtime.expressions.TExpression;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Compiler for Mach machine.
 */
public final class MachCompiler {
    /**
     * Expression compilation result.
     */
    private class ExpressionCompilation {
        private TExpression expression;
        private int numVariables = 0;
    }
//    /**
//     * Contains data when building a type.
//     */
//    private static class TypeBuildContext {
//        private TType.Builder builder;
//        private TConstructor internalConstructor;
//    }
//
//    /**
//     * Contains data when compiling.
//     */
//    private static class CompilationContext {
//        private Scope scope;
//        private MethodBase method;
//        private TypeBuildContext typeBuildContext;
//
//        private Block block;
//        private Stack<Block> blocks = new Stack<>();
//
//        private Map<Object, Scope> scopes = new HashMap<>();
//
//        /**
//         * Push block.
//         *
//         * @param block the block
//         */
//        public void pushBlock(Block block) {
//            blocks.push(block);
//        }
//
//        /**
//         * Pop the last block.
//         *
//         * @return the block
//         */
//        public Block popBlock() {
//            if (blocks.isEmpty())
//                return null;
//            return blocks.pop();
//        }
//
//        /**
//         * Current block.
//         *
//         * @return the block
//         */
//        public Block currentBlock() {
//            return blocks.peek();
//        }
//
//        /**
//         * Gets scope.
//         *
//         * @return the scope
//         */
//        public Scope getScope() {
//            return scope;
//        }
//
//        /**
//         * Gets method.
//         *
//         * @return the method
//         */
//        public MethodBase getMethod() {
//            return method;
//        }
//
//        /**
//         * Push type scope.
//         *
//         * @param type the type
//         */
//        public void pushTypeScope(TType type) {
//            this.scope = scopes.computeIfAbsent(type, (k) -> new TypeScope(this.scope, type));
//            this.method = null;
//        }
//
//        /**
//         * Push method scope.
//         *
//         * @param method the method
//         */
//        public void pushMethodScope(MethodBase method) {
//            this.scope = scopes.computeIfAbsent(method, (k) -> new MethodScope(this.scope, method));
//            this.method = method;
//        }
//
//        /**
//         * Pop scope.
//         */
//        public void popScope() {
//            if (scope != null) {
//                scope = scope.getParent();
//
//                if (scope != null) {
//                    this.method = (scope instanceof MethodScope) ? ((MethodScope)scope).getMethod() : null;
//                }
//                else
//                    this.method = null;
//            }
//        }
//    }

//    private List<TypeBuildContext> typeBuildContexts = new ArrayList<>();
//    private CompilationContext compilationContext = new CompilationContext();
//    private List<TType> types = new ArrayList<>();

    private ProgramInfo rootScope;
    private Scope currentScope;

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

        this.rootScope = new ProgramInfo();

        // Build type declarations, including their functions.
        for (GUnit unit : program.getUnits()) {
            UnitInfo unitInfo = new UnitInfo(this.rootScope).setDeclaration(unit);
            this.rootScope.addUnit(unitInfo);

            for (GGraph graph : unit.getGraphs()) {
                TypeInfo typeInfo = new TypeInfo(unitInfo);
                typeInfo.setDeclaration(graph).setName(graph.getName());
                unitInfo.addType(typeInfo);

                for (GMethod method: graph.getMethods()) {
                    MethodInfo methodInfo = new MethodInfo(typeInfo);
                    methodInfo.setName(method.getName()).setDeclaration(method);
                    typeInfo.addMethod(methodInfo);
                }
            }
        }

        // Build field declarations and default internal constructor.
        for (UnitInfo unit: rootScope.getUnits()) {
            for (TypeInfo type: unit.getTypes()) {
                GGraph graph = type.getDeclaration();

                for (GGraphField graphField: graph.getFields()) {
                    FieldInfo field = new FieldInfo(type);
                    field.setDeclaration(graphField).setName(graphField.getName());

                    // Initialize field in internal constructor.
                    initializeField(field);

                    // Add the field to the type.
                    type.addField(field);
                }
            }
        }

//
//        // Field declaration.
//        for (TypeBuildContext context: typeBuildContexts) {
//            compilationContext.typeBuildContext = context;
//
//            GGraph graph = context.builder.getDeclaration();
//
//            for (GGraphField field: graph.getFields()) {
//                if (field instanceof GField) {
//                    context.builder.createField()
//                            .setDeclaration(field)
//                            .setName(field.getName())
//                            .setType(TType.OBJECT_TYPE)
//                            .build();
//                }
//            }
//        }
//
//        // Compile internal constructor.
//        for (TypeBuildContext context: typeBuildContexts)
//            compileInternalConstructor(context);
//
//        typeBuildContexts.forEach(c -> types.add(c.builder.build()));

        return executable;
    }

    /**
     * Initializes a field within internal compiler scope.
     * @param field the field to initialize
     */
    private void initializeField(FieldInfo field) {
        GGraphField declaration = field.getDeclaration();
        this.currentScope = field.getType().getInternalConstructor().getCode();

        if (declaration instanceof GField) {
            GField fieldDeclaration = (GField)declaration;
            if (fieldDeclaration.getInitializer() == null)
                return;

            __load(0);
            __evaluate(fieldDeclaration.getInitializer());
            // __putField(fi);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Machine instructions.

    /**
     * Loads a local variable on to the stack
     * @param varIndex variable index.
     */
    private void __load(int varIndex) {

    }

    private void __putField(int fieldIndex) {

    }

    /**
     * Evaluates an expression in current scope, push value to current stack.
     * @param expression expression.
     */
    private void __evaluate(GExpression expression) {
        checkArgument(this.currentScope instanceof Block);

        Block block = (Block)currentScope;

    }

//    /**
//     * Begin a method.
//     * @param method
//     */
//    private void beginMethod(MethodBase method) {
//        compilationContext.pushMethodScope(method);
//        compilationContext.block = new Block();
//    }
//
//    /**
//     * End a method.
//     */
//    private void endMethod() {
//        compilationContext.method.instructions = compilationContext.block.flatten();
//
//        compilationContext.popScope();
//        compilationContext.block = null;
//    }
//
//    /**
//     * Compiles the internal constructor.
//     * @param context type build context
//     */
//    private void compileInternalConstructor(TypeBuildContext context) {
//        beginMethod(context.internalConstructor);
//
//        List<TField> fields = context.builder.getFields();
//
//        fields.forEach(f -> buildGraphFieldInitializer(context, f));
//
//        endMethod();
//    }
//
//    private void buildGraphFieldInitializer(TypeBuildContext context, TField field) {
//        GGraphField declaration = field.getDeclaration();
//
//        Block block = compilationContext.currentBlock();
//        // Variable thisObjectRef = compilationContext.getMethod().variables.get(0);
//
//        if (declaration instanceof GField) {
//            GField fieldDeclaration = (GField)declaration;
//            if (fieldDeclaration.getInitializer() == null)
//                return;
//
//
//            block.add(new Load(0));
//            block.add(new Evaluate(compileExpression(fieldDeclaration.getInitializer())));
//            block.add(new StoreField(0));
//        }
//    }
//
//    private void compileAssign(TField lhs, GExpression rhs) {
//        // MethodScope scope =
//    }
//
//    private TExpression compileExpression(GExpression expression) {
//        return new TObjectRef(new TInteger(10));
//    }
}
