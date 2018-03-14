package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;

public class TInteger extends TObject {
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
     * Gets the type of this object.
     *
     * @return object type.
     */
    @Override
    public TType getType() {
        return Types.INTEGER_TYPE;
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
