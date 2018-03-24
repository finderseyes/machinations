package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.VariableInfo;

public class JumpIf extends Instruction {
    private VariableInfo condition;
    private Instruction whenTrue;
    private Instruction whenFalse;

    public VariableInfo getCondition() {
        return condition;
    }

    public JumpIf setCondition(VariableInfo condition) {
        this.condition = condition;
        return this;
    }

    public Instruction getWhenTrue() {
        return whenTrue;
    }

    public JumpIf setWhenTrue(Instruction whenTrue) {
        this.whenTrue = whenTrue;
        return this;
    }

    public Instruction getWhenFalse() {
        return whenFalse;
    }

    public JumpIf setWhenFalse(Instruction whenFalse) {
        this.whenFalse = whenFalse;
        return this;
    }
}
