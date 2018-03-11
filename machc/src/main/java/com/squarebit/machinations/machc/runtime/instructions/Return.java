package com.squarebit.machinations.machc.runtime.instructions;

/**
 * Returns from current method, and push its return value onto stack if yet.
 */
public class Return {
    private boolean haveReturnValue;

    /**
     * Instantiates a new return instruction.
     *
     * @param haveReturnValue if true, the current value on operand stack is popped and push onto caller stack before
     *                        returning, or simply returns to the caller otherwise.
     */
    public Return(boolean haveReturnValue) {
        this.haveReturnValue = haveReturnValue;
    }


}
