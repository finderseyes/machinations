package com.squarebit.machinations.machc.runtime.instructions;

import com.squarebit.machinations.machc.runtime.MachInstruction;
import com.squarebit.machinations.machc.runtime.expressions.TExpression;

/**
 * Evaluates an expression and push its result onto the stack.
 */
public final class Evaluate {
    private final TExpression expression;

    /**
     * Instantiates a new instance.
     *
     * @param expression the expression to evaluate
     */
    public Evaluate(TExpression expression) {
        this.expression = expression;
    }
}
