package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;
import com.squarebit.machinations.machc.vm.TypeInfo;

public final class TType extends TObject {
    private final TypeInfo typeInfo;

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

    /**
     * Determines if this type is the same or a supertype of given type.
     *
     * @param type the type
     * @return true or false
     */
    public boolean isAssignableFrom(TType type) {
        return this.typeInfo.isAssignableFrom(type.typeInfo);
    }
}
