package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.VariableInfo;
import com.squarebit.machinations.machc.avm.expressions.Expression;

public class Evaluate extends Instruction {
    private Expression expression;
    private VariableInfo result;

    public Evaluate(Expression expression, VariableInfo result) {
        this.expression = expression;
        this.result = result;
    }

    public Expression getExpression() {
        return expression;
    }

    public VariableInfo getResult() {
        return result;
    }
}
