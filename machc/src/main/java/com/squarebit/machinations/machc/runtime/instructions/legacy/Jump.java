package com.squarebit.machinations.machc.runtime.instructions.legacy;

import com.squarebit.machinations.machc.runtime.Instruction;

/**
 * In-frame jump.
 */
public final class Jump extends Instruction {
    private Label target;

    public Jump(Label target) {
        this.target = target;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public Label getTarget() {
        return target;
    }

    /**
     * Execute the current instruction.
     */
    @Override
    public void execute() {}
}
