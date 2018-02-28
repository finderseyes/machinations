package com.squarebit.machinations.engine;

public class Not extends LogicalExpression {
    private LogicalExpression expression;

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public LogicalExpression getExpression() {
        return expression;
    }

    /**
     * Sets expression.
     *
     * @param expression the expression
     * @return the expression
     */
    public Not setExpression(LogicalExpression expression) {
        this.expression = expression;
        return this;
    }

    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean eval() {
        return !expression.eval();
    }

    public static Not of(LogicalExpression expression) {
        return (new Not().setExpression(expression));
    }
}
