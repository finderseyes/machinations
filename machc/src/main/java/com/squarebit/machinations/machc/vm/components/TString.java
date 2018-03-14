package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;

/**
 * The type T string.
 */
public final class TString extends TObject {
    private final String value;

    /**
     * Instantiates a new T string.
     *
     * @param value the value
     */
    public TString(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the type of this object.
     *
     * @return object type.
     */
    @Override
    public TType getType() {
        return Types.STRING_TYPE;
    }
}
