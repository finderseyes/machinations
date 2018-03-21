package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.runtime.TObject;

import java.util.concurrent.CompletableFuture;

public final class NativeMethodFrame extends Frame {
    private CompletableFuture<TObject> returnFuture;

    /**
     * Instantiates a new Native method frame.
     *
     * @param caller       the caller
     * @param returnFuture the return future
     */
    public NativeMethodFrame(Frame caller, CompletableFuture<TObject> returnFuture) {
        super(caller);
        this.returnFuture = returnFuture;
    }

    /**
     * Gets the number of variable.
     *
     * @return number of local variables.
     */
    @Override
    public int getLocalVariableCount() {
        return 0;
    }

    /**
     * Gets return future.
     *
     * @return the return future
     */
    public CompletableFuture<TObject> getReturnFuture() {
        return returnFuture;
    }
}
