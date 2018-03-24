package com.squarebit.machinations.machc.ast.expressions;

/**
 * The integer literal.
 */
public final class GInteger implements GExpression {
    private final int value;

    /**
     * Instantiates a new integer expression.
     *
     * @param value the expression value
     */
    public GInteger(int value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Parse g integer.
     *
     * @param text the text
     * @return the g integer
     */
    public static GInteger parse(String text) {
        int value = Integer.parseInt(text);
        return new GInteger(value);
    }
}
