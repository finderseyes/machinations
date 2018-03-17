package com.squarebit.machinations.machc.avm.runtime;

import java.util.concurrent.CompletableFuture;

public final class NativeMethodFrame {
    private NativeMethodFrame caller;
    private CompletableFuture returnFuture;

    public NativeMethodFrame() {
        this.returnFuture = new CompletableFuture<>();
    }

    public NativeMethodFrame getCaller() {
        return caller;
    }

    public NativeMethodFrame setCaller(NativeMethodFrame caller) {
        this.caller = caller;
        return this;
    }

    public CompletableFuture getReturnFuture() {
        return returnFuture;
    }

    public NativeMethodFrame setReturnFuture(CompletableFuture returnFuture) {
        this.returnFuture = returnFuture;
        return this;
    }
}
