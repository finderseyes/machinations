package com.squarebit.machinations.machc.runtime.instructions;

import com.squarebit.machinations.machc.runtime.Frame;

/**
 * Base class of an instruction.
 */
public abstract class Instruction {
    private Frame frame;

    public Frame getFrame() {
        return frame;
    }

    /**
     * Execute the current instruction.
     */
    public abstract void execute();
}
