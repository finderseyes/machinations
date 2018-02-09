package com.squarebit.machinations.engine;

public class IntNumber extends ArithmeticExpression {
    private int value;

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     * @return the value
     */
    public IntNumber setValue(int value) {
        this.value = value;
        return this;
    }

    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int evaluate() {
        return this.value;
    }

    /**
     * Of number.
     *
     * @param value the value
     * @return the number
     */
    public static IntNumber of(int value) {
        return new IntNumber().setValue(value);
    }
}
