package com.squarebit.machinations.machc.runtime.components;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class of all Mach-machine runtime class.
 */
public class TObject {
    TType type;
    Map<TField, TObject> fieldTableEX = new HashMap<>();
    TObject[] fieldTable;

    /**
     * Gets the object type.
     *
     * @return the object type
     */
    public TType getType() {
        return type;
    }
}
