package com.squarebit.machinations.machc.vm.instructions;

import com.squarebit.machinations.machc.vm.*;

/**
 * The type Load field.
 */
public final class LoadField extends Instruction {
    private final FieldInfo field;

    public LoadField(FieldInfo field) {
        this.field = field;
    }

    /**
     * Execute the instruction given machine instruction context.
     *
     * @param context the instruction context
     */
    @Override
    public void execute(InstructionContext context) {
        MachineContext machineContext = context.getMachineContext();
        TObject instance = machineContext.popStack();
        TObject value = field.get(instance);
        machineContext.pushStack(value);
    }
}
