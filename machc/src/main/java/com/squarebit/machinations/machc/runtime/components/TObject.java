package com.squarebit.machinations.machc.runtime.components;

/**
 * Base class of all Mach-machine runtime class.
 */
public abstract class TObject {
    TType type;
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
