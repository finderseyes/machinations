package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.runtime.TObject;

/**
 * Call frame of a method.
 */
public final class MethodFrame extends Frame {
    private MethodInfo method;
    private TObject returnValue;

    /**
     * Instantiates a new Method frame.
     *
     * @param caller the caller frame
     * @param offset the frame data offset
     * @param method the method
     */
    public MethodFrame(Frame caller, int offset, MethodInfo method) {
        super(caller, offset);
        this.method = method;
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public MethodInfo getMethod() {
        return method;
    }

    /**
     * Gets return value.
     *
     * @return the return value
     */
    public TObject getReturnValue() {
        return returnValue;
    }

    /**
     * Sets return value.
     *
     * @param returnValue the return value
     * @return the return value
     */
    public MethodFrame setReturnValue(TObject returnValue) {
        this.returnValue = returnValue;
        return this;
    }
}
