package com.squarebit.machinations.machc.avm;

/**
 * A call frame of the Abstract Virtual Machine.
 */
public abstract class Frame {
    private Frame caller;
    private int offset;

    /**
     * Instantiates a new Frame.
     *
     * @param caller the caller frame
     * @param offset the offset of the frame in the data stack
     */
    protected Frame(Frame caller, int offset) {
        this.caller = caller;
        this.offset = offset;
    }

    /**
     * Gets the caller frame.
     *
     * @return the caller frame
     */
    public Frame getCaller() {
        return caller;
    }

    /**
     * Gets frame data offset.
     *
     * @return the frame data offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Gets the number of variable.
     * @return number of local variables.
     */
    public abstract int getLocalVariableCount();
}
