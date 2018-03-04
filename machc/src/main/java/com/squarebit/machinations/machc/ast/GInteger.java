package com.squarebit.machinations.machc.ast;

/**
 * The integer literal.
 */
public final class GInteger extends GExpression {
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
    public GInteger setValue(int value) {
        this.value = value;
        return this;
    }
}
