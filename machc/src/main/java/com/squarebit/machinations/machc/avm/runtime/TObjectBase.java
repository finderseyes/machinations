package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.FieldInfo;
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
     * Gets field.
     *
     * @param fieldInfo the field info
     * @return the field
     */
    public final TObject getField(FieldInfo fieldInfo) {
        onGetField(fieldInfo);
        return __fieldTable[fieldInfo.getIndex()];
    }

    /**
     * Called upon a field is accessed.
     * @param fieldInfo
     */
    protected void onGetField(FieldInfo fieldInfo) {}

    /**
     * Sets a field.
     *
     * @param fieldInfo the field info
     * @param value     the value
     * @return the field
     */
    public final TObjectBase setField(FieldInfo fieldInfo, TObject value) {
        onSetField(fieldInfo, value);
        __fieldTable[fieldInfo.getIndex()] = value;
        return this;
    }

    /**
     * Called upon a field is set.
     * @param fieldInfo
     * @param value
     */
    protected void onSetField(FieldInfo fieldInfo, TObject value) {}
}
