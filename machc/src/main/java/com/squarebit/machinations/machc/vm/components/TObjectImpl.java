package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;

/**
 * The type T object.
 */
public class TObjectImpl extends TObject {
    /**
     * Gets the type of this object.
     *
     * @return object type.
     */
    @Override
    public TType getType() {
        return Types.OBJECT_TYPE;
    }
}
