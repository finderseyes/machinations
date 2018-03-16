package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.FieldInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

public class LoadField extends Instruction {
    private VariableInfo fieldOwner;
    private FieldInfo fieldInfo;
    private VariableInfo returnValue;

    public LoadField(VariableInfo fieldOwner, FieldInfo fieldInfo, VariableInfo returnValue) {
        this.fieldOwner = fieldOwner;
        this.fieldInfo = fieldInfo;
        this.returnValue = returnValue;
    }

    public VariableInfo getFieldOwner() {
        return fieldOwner;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public VariableInfo getReturnValue() {
        return returnValue;
    }
}
