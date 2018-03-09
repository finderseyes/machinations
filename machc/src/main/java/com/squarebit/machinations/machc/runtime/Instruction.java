package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.runtime.Frame;

/**
 * Base class of an instruction.
 */
public abstract class Instruction {
    Frame frame;

    public Frame getFrame() {
        return frame;
    }

    /**
     * Execute the current instruction.
     */
    public abstract void execute() throws Exception;
}
