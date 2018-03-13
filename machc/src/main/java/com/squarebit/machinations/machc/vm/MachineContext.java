package com.squarebit.machinations.machc.vm;

import java.util.Stack;

/**
 * The type Execution data.
 */
public final class MachineContext {
    private Stack<TObject> dataStack = new Stack<>();
    private Stack<Frame> callStack = new Stack<>();

    /**
     * Gets a local variable within current stack frame.
     * @param index index of the local variable
     * @return local variable value.
     */
    public TObject getLocalVar(int index) {
        return dataStack.get(index + getCurrentFrame().getStackOffset());
    }

    /**
     * Push stack.
     *
     * @param item the item
     */
    public void pushStack(TObject item) {
        dataStack.push(item);
    }

    /**
     * Pop stack t object.
     *
     * @return the t object
     */
    public TObject popStack() {
        return dataStack.pop();
    }

    /**
     * Gets current frame.
     *
     * @return the frame
     */
    public Frame getCurrentFrame() {
        if (callStack.isEmpty())
            return null;
        else
            return callStack.peek();
    }

    /**
     * Push frame frame.
     *
     * @param method the method
     * @return the frame
     */
    public Frame pushFrame(MethodInfo method) {
        int argumentVariableCount = method.argumentVariableCount();
        int localVariableCount = method.variableCount() - argumentVariableCount;
        int stackOffset = dataStack.size() - argumentVariableCount;

        // Push stack frame for local variables.
        for (int i = 0; i < localVariableCount; i++)
            dataStack.push(null);

        Frame frame = new Frame().setStackOffset(stackOffset).setMethod(method);

        // Push the frame to the call stack.
        callStack.push(frame);

        return frame;
    }

    /**
     * Pop frame.
     */
    public void popFrame() {
        Frame frame = getCurrentFrame();
        if (frame == null)
            return;

        MethodInfo method = frame.getMethod();
        int variableCount = method.variableCount();

        // Pop method's local variables.
        for (int i = 0; i < variableCount; i++) {
            dataStack.pop();
        }

        if (method.doesReturnValue())
            pushStack(frame.getReturnValue());

        frame.getFrameReturn().complete(frame.getReturnValue());

        callStack.pop();
    }

    /**
     * Determins if the call stack is empty.
     *
     * @return true or false.
     */
    public boolean isCallStackEmpty() {
        return callStack.isEmpty();
    }
}
