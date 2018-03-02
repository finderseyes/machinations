package com.squarebit.machinations.models;

public enum TimeMode {
    ASYNCHRONOUS,
    SYNCHRONOUS,
    TURN_BASED;

    public static final String K_ASYNCHRONOUS = "asynchronous";
    public static final String K_SYNCHRONOUS = "synchronous";
    public static final String K_TURN_BASED = "turnBased";

    public static TimeMode from(String value) throws Exception {
        if (value == null || value.equals(""))
            return ASYNCHRONOUS;
        else if (value.equals(K_ASYNCHRONOUS))
            return ASYNCHRONOUS;
        else if (value.equals(K_SYNCHRONOUS))
            return SYNCHRONOUS;
        else if (value.equals(K_TURN_BASED))
            return TURN_BASED;
        else
            throw new Exception(String.format("Invalid flow mode %s", value));
    }
}
