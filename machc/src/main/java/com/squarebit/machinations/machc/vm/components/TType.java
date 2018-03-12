package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;
import com.squarebit.machinations.machc.vm.TypeInfo;

public final class TType extends TObject {
    private TypeInfo typeInfo;

    /**
     * Instantiates a new T type.
     *
     * @param typeInfo the type info
     */
    public TType(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    /**
     * Gets the type of this object.
     *
     * @return object type.
     */
    @Override
    public TType getType() {
        return Types.TYPE_TYPE;
    }
}
