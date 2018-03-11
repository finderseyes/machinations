package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.runtime.Frame;

/**
 * Base class of an instruction.
 */
public abstract class Instruction {
    Frame frame;
    int index;

    public Frame getFrame() {
        return frame;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Execute the current instruction.
     */
    public void execute() throws Exception {}
}
