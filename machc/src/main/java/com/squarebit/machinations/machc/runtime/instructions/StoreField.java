package com.squarebit.machinations.machc.runtime.instructions;

import com.squarebit.machinations.machc.runtime.MachInstruction;

/**
 * Puts a value to a field of an object.
 * - Pops the value from the operand stacks.
 * - Pops the object reference from the operand stack.
 * - Puts the value to the field of an object.
 */
public final class StoreField extends MachInstruction {
    private int fieldIndex;

    /**
     * Instantiates a new Store field.
     *
     * @param fieldIndex the field index
     */
    public StoreField(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }
}
