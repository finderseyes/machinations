package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.FieldInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

public final class PutField extends Instruction {
    private VariableInfo thisObject;
    private FieldInfo fieldInfo;
    private VariableInfo value;

    public PutField(VariableInfo thisObject, FieldInfo fieldInfo, VariableInfo value) {
        this.thisObject = thisObject;
        this.fieldInfo = fieldInfo;
        this.value = value;
    }

    public VariableInfo getThisObject() {
        return thisObject;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public VariableInfo getValue() {
        return value;
    }
}
