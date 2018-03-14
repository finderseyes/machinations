package com.squarebit.machinations.machc.vm;

/**
 * An instruction or no-op instruction.
 */
abstract class InstructionBase {
    /**
     * Execute the instruction given machine instruction context.
     *
     * @param context the instruction context
     */
    public abstract void execute(InstructionContext context);
}
