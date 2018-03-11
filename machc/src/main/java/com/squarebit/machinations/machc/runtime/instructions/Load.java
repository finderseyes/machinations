package com.squarebit.machinations.machc.runtime.instructions;

import com.squarebit.machinations.machc.runtime.MachInstruction;

/**
 * Loads a local variable and stores its value onto operand stack.
 */
public final class Load extends MachInstruction {
    private final int variableIndex;

    /**
     * Instantiates a new Load.
     *
     * @param variableIndex the variable index
     */
    public Load(int variableIndex) {
        this.variableIndex = variableIndex;
    }

    /**
     * Gets variable index.
     *
     * @return the variable index
     */
    public int getVariableIndex() {
        return variableIndex;
    }
}
