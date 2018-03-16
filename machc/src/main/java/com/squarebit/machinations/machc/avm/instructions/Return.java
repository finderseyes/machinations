package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.VariableInfo;

public final class Return extends Instruction {
    private final VariableInfo value;

    public Return(VariableInfo value) {
        this.value = value;
    }

    public VariableInfo getValue() {
        return value;
    }
}
