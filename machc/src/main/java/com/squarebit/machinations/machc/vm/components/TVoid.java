package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;

public class TVoid extends TObject {
    public static TVoid INSTANCE = new TVoid();

    /**
     * Gets the type of this object.
     *
     * @return object type.
     */
    @Override
    public TType getType() {
        return Types.VOID_TYPE;
    }
}
