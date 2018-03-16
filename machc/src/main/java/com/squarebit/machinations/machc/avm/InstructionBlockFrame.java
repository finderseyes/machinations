package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.instructions.Instruction;

/**
 * Call frame of an instruction block.
 */
public final class InstructionBlockFrame extends Frame {
    private InstructionBlock block;
    private int counter = 0;

    /**
     * Instantiates a new Instruction block frame.
     *
     * @param caller the caller frame
     * @param offset the frame data offset
     * @param block  the instruction block
     */
    public InstructionBlockFrame(Frame caller, int offset, InstructionBlock block) {
        super(caller, offset);
        this.block = block;
    }

    /**
     * Gets the number of variable.
     *
     * @return number of local variables.
     */
    @Override
    public int getLocalVariableCount() {
        return block.getLocalVariableCount();
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
     * Gets counter.
     *
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Sets counter.
     *
     * @param counter the counter
     * @return the counter
     */
    public InstructionBlockFrame setCounter(int counter) {
        this.counter = counter;
        return this;
    }

    /**
     * Returns the next instruction and advances the instruction counter.
     * @return next instruction
     */
    public Instruction next() {
        return this.block.getInstructions().get(counter++);
    }
}
