package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.VariableInfo;
import com.squarebit.machinations.machc.avm.runtime.TObject;

public final class PutConstant extends Instruction {
    private TObject value;
    private VariableInfo to;

    public PutConstant(TObject value, VariableInfo to) {
        this.value = value;
        this.to = to;
    }

    public TObject getValue() {
        return value;
    }

    public VariableInfo getTo() {
        return to;
    }
}
