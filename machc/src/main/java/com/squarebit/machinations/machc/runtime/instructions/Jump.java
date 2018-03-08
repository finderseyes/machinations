package com.squarebit.machinations.machc.runtime.instructions;

import com.squarebit.machinations.machc.runtime.Frame;

public final class Jump extends Instruction {
    final Frame target;

    private Jump(final Frame target) {
        this.target = target;
    }

    public static Jump to(final Frame target) {
        return new Jump(target);
    }

    /**
     * Execute the current instruction.
     */
    @Override
    public void execute() {

    }
}
