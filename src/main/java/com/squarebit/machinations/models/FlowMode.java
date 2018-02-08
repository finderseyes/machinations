package com.squarebit.machinations.models;

public enum FlowMode {
    AUTOMATIC,
    PULL_ANY,
    PUSH_ANY,
    PULL_ALL,
    PUSH_ALL;

    public static final String K_AUTOMATIC = "automatic";
    public static final String K_PULL_ANY = "pullAny";
    public static final String K_PUSH_ANY = "pushAny";
    public static final String K_PULL_ALL = "pullAll";
    public static final String K_PUSH_ALL = "pushAll";

    public static FlowMode from(String value) throws Exception {
        if (value == null)
            return AUTOMATIC;
        else if (value.equals(K_AUTOMATIC))
            return AUTOMATIC;
        else if (value.equals(K_PULL_ANY))
            return PULL_ANY;
        else if (value.equals(K_PUSH_ANY))
            return PUSH_ANY;
        else if (value.equals(K_PULL_ALL))
            return PULL_ALL;
        else if (value.equals(K_PUSH_ALL))
            return PUSH_ALL;
        else
            throw new Exception(String.format("Invalid flow mode %s", value));
    }
}
