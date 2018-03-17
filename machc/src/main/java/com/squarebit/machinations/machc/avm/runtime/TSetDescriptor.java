package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;

public final class TSetDescriptor implements TObject {

    @ConstructorMethod
    public void init(TObject a) {
        int k = 10;
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.SET_DESCRIPTOR_TYPE;
    }
}
