package com.squarebit.machinations.machc.vm.expressions;

import com.squarebit.machinations.machc.vm.TObject;

public final class ExpressionVariable extends Expression {
    private final int index;

    public ExpressionVariable(int index) {
        this.index = index;
    }

    /**
     * Evalutes the expression under given context.
     *
     * @param context the evaluation context
     * @return result.
     */
    @Override
    public TObject evaluate(EvaluationContext context) {
        return context.getVariable(index);
    }
}
