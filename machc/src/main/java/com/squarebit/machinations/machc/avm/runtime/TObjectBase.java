package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.TypeInfo;

/**
 * Implementation of those object having a field table and instantiable by the runtime.
 */
public class TObjectBase implements TObject {
    private TypeInfo __typeInfo;
    private TObject[] __fieldTable;

    /**
     * Instantiates a new instance.
     */
    public TObjectBase() {
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public final TypeInfo getTypeInfo() {
        return __typeInfo;
    }

    /**
     * Gets a field at given index.
     * @param index field index.
     * @return field value
     */
    public TObject getField(int index) {
        return __fieldTable[index];
    }

    /**
     * Sets a field at given index.
     * @param index field index
     * @param value field value
     * @return this instance.
     */
    public TObjectBase setField(int index, TObject value) {
        __fieldTable[index] = value;
        return this;
    }
}
