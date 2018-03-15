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

    public Scope getScope() {
        return scope;
    }

    public Instruction setScope(Scope scope) {
        this.scope = scope;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Instruction setIndex(int index) {
        this.index = index;
        return this;
    }
}
