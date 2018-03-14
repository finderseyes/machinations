package com.squarebit.machinations.engine;

public class Addition extends IntegerExpression {
    private IntegerExpression lhs, rhs;
    private boolean random;

    /**
     * Determines if the expression evaluates to a random value.
     *
     * @return true if the expression value is random, false otherwise
     */
    @Override
    public boolean isRandom() {
        return this.random;
    }

    /**
     * Evaluates the expression to universal numerical type (float).
     *
     * @return value as float
     */
    @Override
    public float evalAsFloat() {
        return (lhs.evalAsFloat() + rhs.evalAsFloat());
    }

    /**
     * Creates an instance of multiplicative flow rate.
     *
     * @param lhs the left hand side expression
     * @param rhs the right hand side expression
     * @return the additive flow rate instance
     */
    public static Addition of(IntegerExpression lhs, IntegerExpression rhs) {
        Addition expression = new Addition();

        expression.lhs = lhs;
        expression.rhs = rhs;
        expression.random = lhs.isRandom() || rhs.isRandom();

        return expression;
    }
}
