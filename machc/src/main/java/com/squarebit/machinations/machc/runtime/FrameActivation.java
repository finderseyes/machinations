package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.runtime.components.TObject;

/**
 * Frame execution context, which contains the variable table, containing realization of frame's variables
 * in run-time.
 */
public final class FrameActivation {
    FrameActivation parent;
    Frame frame;
    TObject[] variableTable;

    /**
     * Get instructions.
     *
     * @return the instructions
     */
    public MachInstruction[] getInstructions() {
        return frame.getInstructions();
    }

    /**
     * Deactivates this activation.
     */
    public void deactivate() {
        frame.deactivate(this);
    }
}
