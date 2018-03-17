package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.Scope;

/**
 * Base class of an instruction executable by Abstract Virtual Machine.
 */
public abstract class Instruction {
    private Scope scope;
    private int index;

    /**
     * Instantiates a new Instruction.
     */
    public Instruction() {
    }

    /**
     * Gets instruction scope.
     *
     * @return the scope
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Sets instruction scope.
     *
     * @param scope the scope
     * @return the scope
     */
    public Instruction setScope(Scope scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Gets instruction index in containing block.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets instruction index in container block.
     *
     * @param index the index
     * @return the index
     */
    public Instruction setIndex(int index) {
        this.index = index;
        return this;
    }
}
