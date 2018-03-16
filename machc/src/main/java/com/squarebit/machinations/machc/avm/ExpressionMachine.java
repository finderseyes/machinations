package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.expressions.Expression;
import com.squarebit.machinations.machc.avm.runtime.TInteger;
import com.squarebit.machinations.machc.avm.runtime.TObject;

final class ExpressionMachine {
    private Machine machine;

    public ExpressionMachine(Machine machine) {
        this.machine = machine;
    }

    public TObject evaluate(Expression expression) {
        return new TInteger(10);
    }
}
