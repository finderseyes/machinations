package com.squarebit.machinations.machc.vm.instructions;

import com.squarebit.machinations.machc.vm.Instruction;

/**
 * Loads a local variable onto stack.
 */
public final class Load extends Instruction {
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
