package com.squarebit.machinations.modelsex;

public enum ActivationMode {
    AUTOMATIC,
    INTERACTIVE,
    STARTING_ACTION,
    PASSIVE;

    public static ActivationMode from(String value) {
        if (value.equals(Constants.ACTIVATION_MODE_AUTOMATIC))
            return AUTOMATIC;
        else if (value.equals(Constants.ACTIVATION_MODE_INTERACTIVE))
            return INTERACTIVE;
        else if (value.equals(Constants.ACTIVATION_MODE_STARTING_ACTION))
            return STARTING_ACTION;
        return PASSIVE;
    }
}
