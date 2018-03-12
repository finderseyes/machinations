package com.squarebit.machinations.machc.vm.instructions;

import com.squarebit.machinations.machc.vm.FieldInfo;
import com.squarebit.machinations.machc.vm.Instruction;

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
}
