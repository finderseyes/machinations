package com.squarebit.machinations.machc.runtime.components;

public final class TFloat extends TObject {
    private final float value;

    /**
     * Instantiates a new T float.
     *
     * @param value the value
     */
    public TFloat(float value) {
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
}
