package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.vm.components.TVoid;

import java.util.concurrent.CompletableFuture;

/**
 * A call stack frame.
 */
public final class Frame {
    private MethodInfo method;

    private TObject returnValue = TVoid.INSTANCE;
    private CompletableFuture<TObject> frameReturn = new CompletableFuture<>();

    private int stackOffset = 0;
    private int instructionCounter = 0;

    public MethodInfo getMethod() {
        return method;
    }

    public Frame setMethod(MethodInfo method) {
        this.method = method;
        return this;
    }

    public int getStackOffset() {
        return stackOffset;
    }

    public Frame setStackOffset(int stackOffset) {
        this.stackOffset = stackOffset;
        return this;
    }

    public int getInstructionCounter() {
        return instructionCounter;
    }

    public Frame setInstructionCounter(int instructionCounter) {
        this.instructionCounter = instructionCounter;
        return this;
    }

    public TObject getReturnValue() {
        return returnValue;
    }

    public Frame setReturnValue(TObject returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    /**
     * Gets frame return.
     *
     * @return the frame return
     */
    public CompletableFuture<TObject> getFrameReturn() {
        return frameReturn;
    }
}
