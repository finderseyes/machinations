package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.FieldInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

/**
 * Loads a field of an object to a local variable.
 */
public class LoadField extends Instruction {
    private final FieldInfo fieldInfo;
    private final VariableInfo instance;
    private final VariableInfo to;

    /**
     * Instantiates a new Load field.
     *
     * @param fieldInfo the field info
     * @param instance  the instance
     * @param to        the to
     */
    public LoadField(FieldInfo fieldInfo, VariableInfo instance, VariableInfo to) {
        this.fieldInfo = fieldInfo;
        this.instance = instance;
        this.to = to;
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
     * Gets to.
     *
     * @return the to
     */
    public VariableInfo getTo() {
        return to;
    }
}
