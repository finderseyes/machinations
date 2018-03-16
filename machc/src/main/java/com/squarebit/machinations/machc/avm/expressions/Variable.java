package com.squarebit.machinations.machc.avm.expressions;

import com.squarebit.machinations.machc.avm.VariableInfo;

public class Variable extends Expression {
    private VariableInfo variableInfo;

    public Variable(VariableInfo variableInfo) {
        this.variableInfo = variableInfo;
    }

    public VariableInfo getVariableInfo() {
        return variableInfo;
    }
}
