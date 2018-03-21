package com.squarebit.machinations.machc.avm;

/**
 * A call frame of the Abstract Virtual Machine.
 */
public abstract class Frame {
    private Frame caller;
    private NativeMethodFrame activeNativeMethodFrame;

    /**
     * Instantiates a new Frame.
     *
     * @param caller the caller frame
     */
    Frame(Frame caller) {
        this.caller = caller;
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
     * Gets the number of variable.
     * @return number of local variables.
     */
    public abstract int getLocalVariableCount();

    /**
     * Gets native call stack.
     *
     * @return the native call stack
     */
    public NativeMethodFrame getActiveNativeMethodFrame() {
        return activeNativeMethodFrame;
    }

    /**
     * Sets native call stack.
     *
     * @param activeNativeMethodFrame the native call stack
     * @return the native call stack
     */
    public Frame setActiveNativeMethodFrame(NativeMethodFrame activeNativeMethodFrame) {
        this.activeNativeMethodFrame = activeNativeMethodFrame;
        return this;
    }

    /**
     * Called back when the frame is exiting, data stack is not popped.
     */
    public void onExiting(Machine machine) {
    }

    /**
     * Called back when the frame is exited, data stack is popped.
     */
    public void onExit(Machine machine) {
    }

    /**
     * On panic.
     *
     * @param exception the exception
     */
    public void onPanic(Exception exception) {

    }
}
