package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.VariableInfo;
import com.squarebit.machinations.machc.avm.runtime.TInteger;

public class PutArray extends Instruction {
    private VariableInfo from;
    private VariableInfo array;
    private TInteger itemIndex;

    public PutArray(VariableInfo from, VariableInfo array, TInteger itemIndex) {
        this.from = from;
        this.array = array;
        this.itemIndex = itemIndex;
    }

    public VariableInfo getFrom() {
        return from;
    }

    public VariableInfo getArray() {
        return array;
    }

    public TInteger getItemIndex() {
        return itemIndex;
    }
}
