package com.squarebit.machinations.machc.ast;

/**
 * Method modifier.
 */
public enum GMethodModifier {
    NONE,
    START,
    AUTOMATIC,
    INTERACTIVE;

    public static GMethodModifier parse(String text) {
        if (text.equals("start")) return START;
        else if (text.equals("automatic")) return AUTOMATIC;
        else if (text.equals("interactive")) return INTERACTIVE;
        else
            return NONE;
    }
}
