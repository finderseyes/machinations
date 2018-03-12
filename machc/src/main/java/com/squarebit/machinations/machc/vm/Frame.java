package com.squarebit.machinations.machc.vm;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Frame {
    private Frame parent;
    private MethodInfo method;
    private CompletableFuture<TObject> returnValue = new CompletableFuture<>();

    private int stackOffset;
    private int instructionCounter = 0;

    public Frame getParent() {
        return parent;
    }

    public Frame setParent(Frame parent) {
        this.parent = parent;
        return this;
    }

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

    public CompletableFuture<TObject> getReturnValue() {
        return returnValue;
    }

    public Frame setReturnValue(CompletableFuture<TObject> returnValue) {
        this.returnValue = returnValue;
        return this;
    }
}
