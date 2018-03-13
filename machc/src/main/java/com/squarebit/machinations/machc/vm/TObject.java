package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.vm.components.TType;

/**
 * The object interface.
 */
public abstract class TObject {
    TObject[] __fields__;

    /**
     * Gets the type of this object.
     * @return object type.
     */
    public abstract TType getType();
}
