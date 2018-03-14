package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;

/**
 * The type T boolean.
 */
public final class TBoolean extends TObject {
    public static final TBoolean TRUE = new TBoolean(true);
    public static final TBoolean FALSE = new TBoolean(false);

    private final boolean value;

    /**
     * Instantiates a new T boolean.
     *
     * @param value the value
     */
    private TBoolean(boolean value) {
        this.value = value;
    }

    /**
     * Is value boolean.
     *
     * @return the boolean
     */
    public boolean getValue() {
        return value;
    }

    /**
     * Gets the type of this object.
     *
     * @return object type.
     */
    @Override
    public TType getType() {
        return Types.BOOLEAN_TYPE;
    }

    /**
     * From t boolean.
     *
     * @param value the value
     * @return the t boolean
     */
    public static TBoolean from(boolean value) {
        if (value)
            return TRUE;
        else
            return FALSE;
    }
}
