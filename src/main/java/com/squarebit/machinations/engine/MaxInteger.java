package com.squarebit.machinations.engine;

/**
 * The maximum integer.
 */
public class MaxInteger extends IntegerExpression {
    private static final MaxInteger INSTANCE = new MaxInteger();

    /**
     * Determines if the expression evaluates to a random value.
     *
     * @return true if the expression value is random, false otherwise
     */
    @Override
    public boolean isRandom() {
        return false;
    }

    /**
     * Evaluates the flow rate.
     *
     * @return integer value of the flow rate.
     */
    @Override
    public int eval() {
        return Integer.MAX_VALUE;
    }

    /**
     * Evaluates the expression to universal numerical type (float).
     *
     * @return value as float
     */
    @Override
    public float evalAsFloat() {
        return this.eval();
    }

    /**
     * Gets the maximum integer instance.
     * @return the instance
     */
    public static MaxInteger instance() {
        return INSTANCE;
    }
}
