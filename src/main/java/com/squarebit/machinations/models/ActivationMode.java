package com.squarebit.machinations.models;

public enum ActivationMode {
    AUTOMATIC,
    INTERACTIVE,
    STARTING_ACTION,
    PASSIVE;

    public static final String K_AUTOMATIC = "automatic";
    public static final String K_INTERACTIVE = "interactive";
    public static final String K_STARTING_ACTION = "startingAction";
    public static final String K_PASSIVE = "passive";

    public static ActivationMode from(String value) throws Exception {
        if (value == null)
            return PASSIVE;
        else if (value.equals(K_AUTOMATIC))
            return AUTOMATIC;
        else if (value.equals(K_INTERACTIVE))
            return INTERACTIVE;
        else if (value.equals(K_STARTING_ACTION))
            return STARTING_ACTION;
        else if (value.equals(K_PASSIVE))
            return PASSIVE;
        else
            throw new Exception(String.format("Invalid activation mode %s", value));
    }
}

