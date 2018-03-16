package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;

public class TFloat implements TObject {
    private float value;

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

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.FLOAT_TYPE;
    }
}
