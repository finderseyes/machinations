package com.squarebit.machinations.machc.vm.instructions;

import com.squarebit.machinations.machc.vm.Instruction;
import com.squarebit.machinations.machc.vm.expressions.Expression;

/**
 * Evaluates an expression and puts result back on the stack.
 */
public class Evaluate extends Instruction {
    private final Expression expression;
    private final int variableCount;

    /**
     * Instantiates a new Evaluate.
     *
     * @param expression    the expression
     * @param variableCount the variable count
     */
    public Evaluate(Expression expression, int variableCount) {
        this.expression = expression;
        this.variableCount = variableCount;
    }

    /**
     * Instantiates a new Evaluate.
     *
     * @param expression the expression
     */
    public Evaluate(Expression expression) {
        this(expression, 0);
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        return expression;
    }

    /**
     * Gets variable count.
     *
     * @return the variable count
     */
    public int getVariableCount() {
        return variableCount;
    }
}
