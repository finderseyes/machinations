package com.squarebit.machinations.machc.ast.expressions;

/**
 * The integer literal.
 */
public final class GInteger extends GExpression {
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
}
