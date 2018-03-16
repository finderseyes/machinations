package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;

public class TBoolean implements TObject {
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
     * From t boolean.
     *
     * @param value the value
     * @return the t boolean
     */
    public static TBoolean from(boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.BOOLEAN_TYPE;
    }
}
