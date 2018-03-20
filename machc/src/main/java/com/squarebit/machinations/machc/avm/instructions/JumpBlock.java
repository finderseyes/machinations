package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.InstructionBlock;

public class JumpBlock extends Instruction {
    private InstructionBlock block;

    public JumpBlock(InstructionBlock block) {
        this.block = block;
    }
}
