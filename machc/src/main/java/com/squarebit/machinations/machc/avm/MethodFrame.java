package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.runtime.TNaN;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.TVoid;

import java.util.concurrent.CompletableFuture;

/**
 * Call frame of a method.
 */
public final class MethodFrame extends Frame implements DataFrame {
    private int offset;
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
        super(caller);
        this.offset = offset;
        this.method = method;
        this.returnFuture = new CompletableFuture<>();
        this.returnValue = TNaN.INSTANCE;
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

    /**
     * On exited.
     */
    @Override
    public void onExit(Machine machine) {
        this.returnFuture.complete(this.returnValue);
    }
}
