package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.TypeInfo;

/**
 * Base class of all runtime/data class.
 */
public interface TObject {
    /**
     * Gets information of the type of this class.
     * @return the {@link TypeInfo}
     */
    TypeInfo getTypeInfo();
}
