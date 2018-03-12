package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.vm.components.TVoid;

import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * The type Execution data.
 */
public final class MachineContext {
    Stack<TObject> dataStack = new Stack<>();
    Stack<Frame> callStack = new Stack<>();

    /**
     *
     * @param index
     * @return
     */
    public TObject getLocalVar(int index) {
        return dataStack.get(index);
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
    public Frame pushFrame(MethodInfo method, boolean methodArgsAreInStack) {
        Frame frame = new Frame();
        int stackOffset;

        if (methodArgsAreInStack)
            stackOffset = dataStack.size() - method.getVariables().size();
        else
            stackOffset = dataStack.size();

        frame.setParent(getCurrentFrame())
                .setStackOffset(stackOffset)
                .setMethod(method);

        callStack.push(frame);

        return frame;
    }

    /**
     * Pop frame.
     */
    public void popFrame(boolean alsoPopMethodArgs) {
        Frame frame = getCurrentFrame();
        if (frame == null)
            return;

        if (alsoPopMethodArgs) {
            int variableCount = frame.getMethod().getVariables().size();
            for (int i = 0; i < variableCount; i++)
                dataStack.pop();
        }

        callStack.pop();

        CompletableFuture<TObject> returnValue = frame.getReturnValue();

        if (!returnValue.isDone())
            returnValue.complete(TVoid.INSTANCE);
    }
}
