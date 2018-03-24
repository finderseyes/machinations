package com.squarebit.machinations.machc.avm.expressions;

/**
 * Logical not.
 */
public class Not extends Expression {
    private final Expression expression;

    /**
     * Instantiates a new Not.
     *
     * @param expression the expression
     */
    public Not(Expression expression) {
        this.expression = expression;
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        return expression;
    }
}
