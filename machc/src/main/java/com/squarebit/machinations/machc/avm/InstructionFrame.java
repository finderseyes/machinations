package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.instructions.Instruction;

/**
 *
 */
public abstract class InstructionFrame extends Frame {
    protected InstructionBlock block;

    /**
     * Initializes a new instance.
     * @param caller the caller frame.
     */
    public InstructionFrame(Frame caller) {
        super(caller);
    }

    /**
     * Gets block.
     *
     * @return the block
     */
    public InstructionBlock getBlock() {
        return block;
    }

    /**
     * Sets block.
     *
     * @param block the block
     * @return the block
     */
    protected InstructionFrame setBlock(InstructionBlock block) {
        this.block = block;
        return this;
    }

    /**
     * Determines if the frame has more instructions to execute.
     * @return true if the frame has one or more instructions to execute
     */
    abstract boolean hasNext();

    /**
     * Gets the next instruction to execute.
     * @return
     */
    abstract Instruction next();

    /**
     * Gets the scope of a frame.
     * @param frame frame.
     * @return scope
     */
    Scope getScope(Frame frame) {
        if (frame == null)
            return null;
        else if (frame instanceof MethodFrame)
            return ((MethodFrame)frame).getMethod();
        else
            return ((InstructionFrame)frame).block;
    }
}
