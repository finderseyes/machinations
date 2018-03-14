package com.squarebit.machinations.machc.vm;

/**
 * No-op instruction.
 */
public class NoOp extends InstructionBase {
    /**
     * Execute the instruction given machine instruction context.
     *
     * @param context the instruction context
     */
    @Override
    public void execute(InstructionContext context) {
        // Do nothing.
    }
}
