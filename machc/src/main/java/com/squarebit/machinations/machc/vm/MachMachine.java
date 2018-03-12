package com.squarebit.machinations.machc.vm;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Mach machine.
 */
public final class MachMachine {
    private MachineContext machineContext = new MachineContext();

    public CompletableFuture<TObject> newInstance(TypeInfo type) throws InstantiationException, IllegalAccessException {
        TObject instance = (TObject)type.getImplementation().newInstance();
        instance.__fields__ = new TObject[type.getFields().size()];
        return machInvoke(type.getInternalConstructor(), instance).thenApply(v -> instance);
    }

    public CompletableFuture<TObject> machInvoke(MethodInfo method, TObject instance, TObject ... args) {
        Frame frame = machineContext.pushFrame(method, false);
        machineContext.pushStack(instance);

        for (TObject arg: args) {
            machineContext.pushStack(arg);
        }

        return frame.getReturnValue();
    }

    public void runOneStep() {
        Frame frame = null;
        InstructionBase instruction = null;

        while (!machineContext.callStack.isEmpty() && instruction == null) {
            frame = machineContext.getCurrentFrame();
            MethodInfo method = frame.getMethod();
            List<InstructionBase> instructions = method.getCode().getInstructions();

            if (frame.getInstructionCounter() < instructions.size()) {
                instruction = instructions.get(frame.getInstructionCounter());
            }
            else {
                machineContext.popFrame(true);
            }
        }

        if (frame != null) {
            executeInstruction(instruction);
            frame.setInstructionCounter(frame.getInstructionCounter() + 1);
        }
    }

    private void executeInstruction(InstructionBase instruction) {
        instruction.execute(new InstructionContext().setMachineContext(machineContext));
    }
}
