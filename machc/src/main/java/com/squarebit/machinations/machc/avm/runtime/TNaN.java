package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;

public final class TNaN implements TObject {
    public static final TNaN INSTANCE = new TNaN();

    /**
     *
     */
    private TNaN() {

    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.NAN_TYPE;
    }
}
