package com.squarebit.machinations.machc.ast.expressions;

public final class GBoolean extends GExpression {
    public static GBoolean TRUE = new GBoolean();
    public static GBoolean FALSE = new GBoolean();

    private GBoolean() {

    }

    /**
     * Parse g boolean.
     *
     * @param text the text
     * @return the g boolean
     */
    public static GBoolean parse(String text) {
        if (Boolean.parseBoolean(text))
            return TRUE;
        else
            return FALSE;
    }
}
