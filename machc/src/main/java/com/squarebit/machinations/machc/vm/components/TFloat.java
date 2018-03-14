package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;

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
     * Gets the type of this object.
     *
     * @return object type.
     */
    @Override
    public TType getType() {
        return Types.FLOAT_TYPE;
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
