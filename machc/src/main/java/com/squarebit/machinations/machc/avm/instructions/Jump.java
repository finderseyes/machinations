package com.squarebit.machinations.machc.avm.instructions;

public class Jump extends Instruction {
    private Instruction to;

    public Jump(Instruction to) {
        this.to = to;
    }

    public Instruction getTo() {
        return to;
    }

    public Jump setTo(Instruction to) {
        this.to = to;
        return this;
    }
}
