package com.squarebit.machinations.machc.vm.expressions;

import com.squarebit.machinations.machc.vm.TObject;

/**
 * And expression.
 */
public abstract class Expression {
    /**
     * Evalutes the expression under given context.
     * @param context the evaluation context
     * @return result.
     */
    public abstract TObject evaluate(EvaluationContext context);
}
