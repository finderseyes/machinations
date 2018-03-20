package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.instructions.Instruction;

/**
 * Call frame of an instruction block.
 */
public final class InstructionBlockFrame extends InstructionFrame {
    private int counter = 0;

    /**
     * Instantiates a new Instruction block frame.
     *
     * @param caller the caller frame
     * @param block  the instruction block
     */
    public InstructionBlockFrame(Frame caller, InstructionBlock block) {
        super(caller);
        this.block = block;
        this.block.setParentScope(getScope(caller));
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
     * Determines if the frame has more instructions to execute.
     *
     * @return true if the frame has one or more instructions to execute
     */
    @Override
    public boolean hasNext() {
        return this.counter < block.getInstructions().size();
    }

    /**
     * Returns the next instruction and advances the instruction counter.
     * @return next instruction
     */
    @Override
    public Instruction next() {
        return this.block.getInstructions().get(counter++);
    }
}
