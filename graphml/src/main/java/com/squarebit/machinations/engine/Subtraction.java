package com.squarebit.machinations.engine;

/**
 * An subtractive flow rate expression.
 */
public class Subtraction extends IntegerExpression {
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
        return (lhs.evalAsFloat() - rhs.evalAsFloat());
    }

    /**
     * Creates an instance of subtractive flow rate.
     *
     * @param lhs the left hand side expression
     * @param rhs the right hand side expression
     * @return the subtractive flow rate instance
     */
    public static Subtraction of(IntegerExpression lhs, IntegerExpression rhs) {
        Subtraction subtractive = new Subtraction();

        subtractive.lhs = lhs;
        subtractive.rhs = rhs;
        subtractive.random = lhs.isRandom() || rhs.isRandom();

        return subtractive;
    }
}
