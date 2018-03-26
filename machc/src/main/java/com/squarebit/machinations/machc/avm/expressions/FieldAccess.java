package com.squarebit.machinations.machc.avm.expressions;

import com.squarebit.machinations.machc.avm.FieldInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

/**
 * Data regarding to a variable, which is caused by evaluating a field.
 */
public class FieldAccess implements VariableData {
    private VariableInfo owner;
    private FieldInfo fieldInfo;

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public VariableInfo getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     * @return the owner
     */
    public FieldAccess setOwner(VariableInfo owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Gets field info.
     *
     * @return the field info
     */
    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    /**
     * Sets field info.
     *
     * @param fieldInfo the field info
     * @return the field info
     */
    public FieldAccess setFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
        return this;
    }
}
