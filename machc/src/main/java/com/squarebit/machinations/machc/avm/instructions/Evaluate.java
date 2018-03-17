package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.VariableInfo;
import com.squarebit.machinations.machc.avm.expressions.Expression;

/**
 * Evaluates an expression and stores the result to a local variable.
 */
public class Evaluate extends Instruction {
    private Expression expression;
    private VariableInfo to;

    /**
     * Instantiates a new Evaluate.
     *
     * @param expression the expression
     * @param to         variable storing evaluation result
     */
    public Evaluate(Expression expression, VariableInfo to) {
        this.expression = expression;
        this.to = to;
    }

    /**
     * Gets evaluated expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        return expression;
    }

    /**
     * Gets result storing variable.
     *
     * @return the variable storing the result
     */
    public VariableInfo getTo() {
        return to;
    }
}
