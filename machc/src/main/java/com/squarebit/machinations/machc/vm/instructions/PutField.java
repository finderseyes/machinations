package com.squarebit.machinations.machc.vm.instructions;

import com.squarebit.machinations.machc.vm.*;

/**
 * Puts a value to an object's field.
 */
public final class PutField extends Instruction {
    private final FieldInfo field;

    /**
     * Instantiates a new PutField instruction.
     *
     * @param field the field to put
     */
    public PutField(FieldInfo field) {
        this.field = field;
    }

    /**
     * Gets the field.
     *
     * @return the field
     */
    public FieldInfo getField() {
        return field;
    }

    /**
     * Execute the instruction given machine instruction context.
     *
     * @param context the instruction context
     */
    @Override
    public void execute(InstructionContext context) {
        MachineContext machineContext = context.getMachineContext();

        TObject value = machineContext.popStack();
        TObject instance = machineContext.popStack();
        field.set(instance, value);
    }
}
