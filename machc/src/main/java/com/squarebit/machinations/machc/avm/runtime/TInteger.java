package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;

/**
 * Integer type.
 */
public final class TInteger implements TObject {
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

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.INTEGER_TYPE;
    }
}
