package com.squarebit.machinations.machc.runtime.components;

public final class TInteger extends TObject {
    private final int value;

    /**
     * Instantiates a new T integer.
     *
     * @param value the value
     */
    public TInteger(int value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
