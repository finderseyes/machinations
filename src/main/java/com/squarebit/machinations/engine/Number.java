package com.squarebit.machinations.engine;

public class Number extends ArithmeticExpression {
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
    public Number setValue(int value) {
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
    public static Number of(int value) {
        return new Number().setValue(value);
    }
}
