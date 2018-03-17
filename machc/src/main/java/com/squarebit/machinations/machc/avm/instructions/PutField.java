package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.FieldInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

/**
 * Puts a local variable to a field of an object instance.
 */
public final class PutField extends Instruction {
    private FieldInfo fieldInfo;
    private VariableInfo instance;
    private VariableInfo from;

    /**
     * Instantiates a new Put field.
     *
     * @param fieldInfo  the field info
     * @param thisObject the this object
     * @param from       the variable from which the field value is set
     */
    public PutField(FieldInfo fieldInfo, VariableInfo thisObject, VariableInfo from) {
        this.instance = thisObject;
        this.fieldInfo = fieldInfo;
        this.from = from;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public VariableInfo getInstance() {
        return instance;
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
     * Gets from.
     *
     * @return the from
     */
    public VariableInfo getFrom() {
        return from;
    }
}
