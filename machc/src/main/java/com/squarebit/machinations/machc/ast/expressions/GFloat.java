package com.squarebit.machinations.machc.ast.expressions;

public final class GFloat extends GExpression {
    private final float value;

    /**
     * Instantiates a new G float.
     *
     * @param value the value
     */
    public GFloat(float value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public float getValue() {
        return value;
    }

    /**
     * Parse g float.
     *
     * @param text the text
     * @return the g float
     */
    public static GFloat parse(String text) {
        return new GFloat(Float.parseFloat(text));
    }
}
