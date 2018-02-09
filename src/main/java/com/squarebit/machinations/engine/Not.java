package com.squarebit.machinations.engine;

public class Not extends BooleanExpression {
    private BooleanExpression expression;

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public BooleanExpression getExpression() {
        return expression;
    }

    /**
     * Sets expression.
     *
     * @param expression the expression
     * @return the expression
     */
    public Not setExpression(BooleanExpression expression) {
        this.expression = expression;
        return this;
    }

    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean evaluate() {
        return !expression.evaluate();
    }

    public static Not of(BooleanExpression expression) {
        return (new Not().setExpression(expression));
    }
}
