package com.squarebit.machinations.machc.avm;

/**
 * Frame which marks the start of current data stack.
 */
public interface DataFrame {
    /**
     * Gets frame data offset.
     *
     * @return the frame data offset
     */
    int getOffset();
}
