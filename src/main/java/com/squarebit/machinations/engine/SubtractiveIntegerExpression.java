package com.squarebit.machinations.engine;

/**
 * An subtractive flow rate expression.
 */
public class SubtractiveIntegerExpression extends IntegerExpression {
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
        return this.lhs.eval() - this.rhs.eval();
    }

    /**
     * Creates an instance of subtractive flow rate.
     *
     * @param lhs the left hand side expression
     * @param rhs the right hand side expression
     * @return the subtractive flow rate instance
     */
    public static SubtractiveIntegerExpression of(IntegerExpression lhs, IntegerExpression rhs) {
        SubtractiveIntegerExpression subtractive = new SubtractiveIntegerExpression();

        subtractive.lhs = lhs;
        subtractive.rhs = rhs;
        subtractive.random = lhs.isRandom() || rhs.isRandom();

        return subtractive;
    }
}
