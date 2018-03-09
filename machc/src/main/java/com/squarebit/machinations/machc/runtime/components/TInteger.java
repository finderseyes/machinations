package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.runtime.components.annotations.NativeMethod;

public final class TInteger extends TObject {
    private int value;

    /**
     * Instantiates a new T integer.
     *
     * @param value the value
     */
    public TInteger(int value) {
        this.value = value;
    }

    /**
     * Instantiates a new integer.
     */
    public TInteger() {
        this.value = 0;
    }

    /**
     * Gets the object type.
     *
     * @return the object type
     */
    @Override
    public TType getType() {
        return TType.INTEGER_TYPE;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    @NativeMethod
    public int foo() {
        int k = 10;
        return k * 10;
    }
}
