package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.VariableInfo;

/**
 * Move/assign a variable to another in the data stack.
 */
public final class Move extends Instruction {
    private final VariableInfo from;
    private final VariableInfo to;

    /**
     * Instantiates a new Move.
     *
     * @param from the from
     * @param to   the to
     */
    public Move(VariableInfo from, VariableInfo to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public VariableInfo getFrom() {
        return from;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public VariableInfo getTo() {
        return to;
    }
}
