package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.runtime.TObject;

import java.util.concurrent.CompletableFuture;

/**
 * Call frame of a method.
 */
public final class MethodFrame extends Frame {
    private MethodInfo method;
    private TObject returnValue;
    private CompletableFuture<TObject> returnFuture;

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
        this.returnFuture = new CompletableFuture<>();
    }

    /**
     * Gets the number of variable.
     *
     * @return number of local variables.
     */
    @Override
    public int getLocalVariableCount() {
        return method.getLocalVariableCount();
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

    /**
     * Gets return future, which completes when the method is popped from the call stack.
     *
     * @return the return future
     */
    public CompletableFuture<TObject> getReturnFuture() {
        return returnFuture;
    }
}
