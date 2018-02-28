package com.squarebit.machinations.engine;

public class NegativeIntegerExpression extends IntegerExpression {
    private IntegerExpression child;

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
        return -child.eval();
    }

    /**
     * Of negative integer expression.
     *
     * @param child the child
     * @return the negative integer expression
     */
    public static NegativeIntegerExpression of(IntegerExpression child) {
        NegativeIntegerExpression expression = new NegativeIntegerExpression();
        expression.child = child;
        return expression;
    }
}
