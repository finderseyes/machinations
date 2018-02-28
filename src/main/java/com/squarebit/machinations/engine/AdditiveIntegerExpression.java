package com.squarebit.machinations.engine;

/**
 * An additive flow rate expression.
 */
public class AdditiveIntegerExpression extends IntegerExpression {
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
     * Evaluates the flow rate.
     *
     * @return integer value of the flow rate.
     */
    @Override
    public int eval() {
        return this.lhs.eval() + this.rhs.eval();
    }

    /**
     * Creates an instance of additive flow rate.
     *
     * @param lhs the left hand side expression
     * @param rhs the right hand side expression
     * @return the additive flow rate instance
     */
    public static AdditiveIntegerExpression of(IntegerExpression lhs, IntegerExpression rhs) {
        AdditiveIntegerExpression additive = new AdditiveIntegerExpression();

        additive.lhs = lhs;
        additive.rhs = rhs;
        additive.random = lhs.isRandom() || rhs.isRandom();

        return additive;
    }
}
