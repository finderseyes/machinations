package com.squarebit.machinations.machc.ast.expressions;

public final class GString implements GExpression {
    private final String value;

    /**
     * Instantiates a new G string.
     *
     * @param value the value
     */
    public GString(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
