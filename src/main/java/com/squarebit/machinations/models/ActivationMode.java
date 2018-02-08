package com.squarebit.machinations.models;

public enum ActivationMode {
    AUTOMATIC,
    INTERACTIVE,
    STARTING_ACTION,
    PASSIVE;

    public static final String ACTIVATION_MODE_AUTOMATIC = "automatic";
    public static final String ACTIVATION_MODE_INTERACTIVE = "interactive";
    public static final String ACTIVATION_MODE_STARTING_ACTION = "startingAction";
    public static final String ACTIVATION_MODE_PASSIVE = "passive";

    public static ActivationMode from(String value) {
        if (value == null)
            return PASSIVE;
        else if (value.equals(ACTIVATION_MODE_AUTOMATIC))
            return AUTOMATIC;
        else if (value.equals(ACTIVATION_MODE_INTERACTIVE))
            return INTERACTIVE;
        else if (value.equals(ACTIVATION_MODE_STARTING_ACTION))
            return STARTING_ACTION;
        else if (value.equals(ACTIVATION_MODE_PASSIVE))
            return PASSIVE;
        else
            return PASSIVE;
    }
}

