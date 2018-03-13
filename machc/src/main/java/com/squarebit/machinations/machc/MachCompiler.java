package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.*;
import com.squarebit.machinations.machc.vm.*;
import com.squarebit.machinations.machc.vm.components.*;
import com.squarebit.machinations.machc.vm.expressions.Expression;
import com.squarebit.machinations.machc.vm.expressions.ObjectRef;
import com.squarebit.machinations.machc.vm.instructions.Evaluate;
import com.squarebit.machinations.machc.vm.instructions.Load;
import com.squarebit.machinations.machc.vm.instructions.PutField;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Compiler for Mach machine.
 */
public final class MachCompiler {
    /**
     * Expression compilation result.
     */
    private class ExpressionCompilation {
        private Expression expression;
        private int variableCount = 0;
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
     * @return the mach program
     * @throws Exception the exception
     */
    public static ProgramInfo compile(GProgram program) {
        return new MachCompiler().doCompile(program);
    }

    /**
     * Performs compilation.
     * @param program the program to compile
     * @return a mach executable
     */
    private ProgramInfo doCompile(GProgram program) {
        this.rootScope = new ProgramInfo();

        // Build type declarations, including their functions.
        for (GUnit unit : program.getUnits()) {
            UnitInfo unitInfo = new UnitInfo(this.rootScope).setDeclaration(unit);
            this.rootScope.addUnit(unitInfo);

            for (GGraph graph : unit.getGraphs()) {
                TypeInfo typeInfo = new TypeInfo(unitInfo);
                typeInfo.setBaseTypeInfo(Types.Internal.RUNTIME_GRAPH_TYPE_INFO)
                        .setImplementation(TRuntimeGraph.class)
                        .setDeclaration(graph).setName(graph.getName());
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
                    field.setIndex(type.getFields().size()).setDeclaration(graphField).setName(graphField.getName());

                    // Initialize field in internal constructor.
                    initializeField(field);

                    // Add the field to the type.
                    type.addField(field);
                }
            }
        }

        //

        return this.rootScope;
    }

    /**
     * Initializes a field within internal compiler scope.
     * @param field the field to initialize
     */
    private void initializeField(FieldInfo field) {
        GGraphField declaration = field.getDeclaration();
        this.currentScope = field.getType().getInternalConstructor().getCode();
        MethodInfo constructor = field.getType().getInternalConstructor();

        if (declaration instanceof GField) {
            GField fieldDeclaration = (GField)declaration;
            if (fieldDeclaration.getInitializer() == null)
                return;

            __load(constructor.getThisVariable());
            __evaluate(fieldDeclaration.getInitializer());
            __putField(field);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Machine instructions.

    /**
     * Loads a local variable on to the stack.
     */
    private void __load(VariableInfo variable) {
        checkArgument(this.currentScope instanceof Block);
        ((Block)this.currentScope).add(new Load(variable.getIndex()));
    }

    /**
     *
     * @param field
     */
    private void __putField(FieldInfo field) {
        checkArgument(this.currentScope instanceof Block);
        ((Block)this.currentScope).add(new PutField(field));
    }

    /**
     * Evaluates an expression in current scope, push value to current stack.
     * @param expression expression.
     */
    private void __evaluate(GExpression expression) {
        checkArgument(this.currentScope instanceof Block);

        ExpressionCompilation expressionCompilation = new ExpressionCompilation();

        if (expression instanceof GInteger)
            compileInteger(expressionCompilation, (GInteger)expression);
        else if (expression instanceof GFloat)
            compileFloat(expressionCompilation, (GFloat)expression);
        else if (expression instanceof GBoolean)
            compileBoolean(expressionCompilation, (GBoolean)expression);
        else if (expression instanceof GString)
            compileString(expressionCompilation, (GString)expression);

        Block block = (Block)currentScope;
        block.add(new Evaluate(expressionCompilation.expression, expressionCompilation.variableCount));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Expression compilation.
    //

    private void compileInteger(ExpressionCompilation compilation, GInteger expression) {
        compilation.expression = new ObjectRef(new TInteger(expression.getValue()));
    }

    private void compileFloat(ExpressionCompilation compilation, GFloat expression) {
        compilation.expression = new ObjectRef(new TFloat(expression.getValue()));
    }

    private void compileBoolean(ExpressionCompilation compilation, GBoolean expression) {
        compilation.expression = new ObjectRef(TBoolean.from(expression == GBoolean.TRUE));
    }

    private void compileString(ExpressionCompilation compilation, GString expression) {
        compilation.expression = new ObjectRef(new TString(expression.getValue()));
    }
}
