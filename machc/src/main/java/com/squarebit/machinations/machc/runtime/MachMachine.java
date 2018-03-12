package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.MachCompiler;
import com.squarebit.machinations.machc.MachExecutable;
import com.squarebit.machinations.machc.ast.GProgram;
import com.squarebit.machinations.machc.runtime.components.TType;

import java.util.Stack;

public final class MachMachine {
    private final GProgram program;
    private final TypeRegistry typeRegistry = new TypeRegistry();
    private final Stack<FrameExecutionContext> executionStack = new Stack<>();

    /**
     * Creates a new machine instance.
     */
    private MachMachine(final GProgram program) {
        this.program = program;
        this.compile();
    }

    /**
     * Creates a new machine using given program specifications.
     * @param program the program specification.
     * @return a machine.
     */
    public static MachMachine from(final GProgram program) {
        MachMachine machine = new MachMachine(program);
        return machine;
    }

    /**
     * Gets a type using its name.
     *
     * @param typeName the type name
     * @return the type
     */
    public TType getType(String typeName) {
        return typeRegistry.getType(typeName);
    }

    /**
     * Compiles current program.
     */
    private void compile() {
        // MachExecutable executable = MachCompiler.compile(this.program);
//        // Build declarations.
//        for (GUnit unit : program.getUnits()) {
//            for (GGraph graph : unit.getGraphs()) {
//                TType.Builder<TRuntimeGraph> builder = new TType.Builder<TRuntimeGraph>()
//                        .setDeclaration(graph)
//                        .setName(graph.getName())
//                        .setBaseType(TType.GRAPH_TYPE)
//                        .setImplementation(TRuntimeGraph.class);
//
//                for (GGraphField field : graph.getFields()) {
//                    if (field instanceof GField) {
//                        builder.addField(
//                                new TField()
//                                        .setDeclaration(field)
//                                        .setName(field.getName())
//                                        .setType(TType.OBJECT_TYPE)
//                        );
//                    }
//                }
//
//                typeRegistry.registerType(builder.build());
//            }
//        }
//
//        // Build code/instructions.
//
//
//
//        Frame frame = buildBootImage();
//        loadFrame(frame);
    }

    /**
     * Loads a frame by pushing its activation to the execution stack.
     *
     * @param frame the frame
     */
    public void loadFrame(Frame frame) {
        FrameActivation activation = frame.activate();
        FrameExecutionContext executionContext = new FrameExecutionContext(activation);
        this.executionStack.push(executionContext);
    }

    /**
     * Executes next instruction in the execution stack.
     */
    public void executeNext() {
        if (this.executionStack.isEmpty())
            return;

        FrameExecutionContext executionContext = null;
        FrameActivation activation = null;
        int nextInstruction = 0;
        MachInstruction[] instructions;

        MachInstruction instruction = null;

        while (!this.executionStack.isEmpty() && instruction == null) {
            executionContext = executionStack.peek();
            activation = executionContext.getActivation();
            nextInstruction = executionContext.getNextInstruction();
            instructions = activation.getInstructions();

            if (nextInstruction >= instructions.length) {
                executionStack.pop();
            }
            else {
                instruction = instructions[nextInstruction];
            }
        }

//        if (instruction != null) {
//            executeInstruction(instruction);
//            executionContext.next();
//        }
    }
//
//    /**
//     * Execute one instruction.
//     * @param instruction the instruction
//     */
//    private void executeInstruction(MachInstruction instruction) {
//        try {
//            if (instruction instanceof Jump) {
//                executeJump((Jump)instruction);
//            }
//            else {
//                instruction.execute();
//            }
//        }
//        catch (Exception ex) {
//
//        }
//    }
//
//    private void executeJump(Jump jump) {
//        if (jump.getFrame() != jump.getTarget().getFrame())
//            throw new RuntimeException("Jump must be in-frame");
//
//        FrameExecutionContext executionContext = executionStack.peek();
//        executionContext.nextInstruction = jump.getTarget().getIndex();
//    }
//
//    /**
//     * Builds the frame that responsible for the main routine.
//     * @return the boot frame
//     */
//    private Frame buildBootImage() {
//        try {
//            Frame.Builder builder = new Frame.Builder();
//
//            VariableInfo mainGraph = builder.declareVariable().setType(TType.INTEGER_TYPE).build();
//
//            builder.addInstruction(new Eval(new TObjectRef(new TInteger(10)), mainGraph));
//
//            Label label = new Label();
//            builder.addInstruction(label);
//
//            Invoke invoke = new Invoke(TType.INTEGER_TYPE.getMethods()[0], mainGraph, new VariableInfo[0]);
//            builder.addInstruction(invoke);
//            builder.addInstruction(new Jump(label));
//
//            return builder.build();
//        }
//        catch (Exception ex) {
//            return null;
//        }
//    }
}
