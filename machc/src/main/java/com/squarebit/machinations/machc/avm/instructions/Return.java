package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.VariableInfo;

/**
 * Breaks the current method and sets a local variable as the returned value.
 */
public final class Return extends Instruction {
    private final VariableInfo value;

    /**
     * Instantiates a new Return.
     *
     * @param value the value
     */
    public Return(VariableInfo value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public VariableInfo getValue() {
        return value;
    }
}
