package com.squarebit.machinations.engine;

public class Negation extends IntegerExpression {
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
     * Evaluates the expression to universal numerical type (float).
     *
     * @return value as float
     */
    @Override
    public float evalAsFloat() {
        return -child.evalAsFloat();
    }

    /**
     * Of negative integer expression.
     *
     * @param child the child
     * @return the negative integer expression
     */
    public static Negation of(IntegerExpression child) {
        Negation expression = new Negation();
        expression.child = child;
        return expression;
    }
}
