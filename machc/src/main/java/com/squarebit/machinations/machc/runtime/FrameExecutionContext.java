package com.squarebit.machinations.machc.runtime;

/**
 * The execution context of a frame.
 */
final class FrameExecutionContext {
    private FrameActivation activation;
    int nextInstruction = 0;

    public FrameExecutionContext(FrameActivation activation) {
        this.activation = activation;
    }

    /**
     * Gets activation.
     *
     * @return the activation
     */
    public FrameActivation getActivation() {
        return activation;
    }

    /**
     * Gets next instruction.
     *
     * @return the next instruction
     */
    public int getNextInstruction() {
        return nextInstruction;
    }

    /**
     * Advances to next instruction.
     */
    public int next() {
        return this.nextInstruction++;
    }
}
