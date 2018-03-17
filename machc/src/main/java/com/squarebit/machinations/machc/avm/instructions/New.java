package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

/**
 * Instantiates a new object of given type.
 */
public final class New extends Instruction {
    private VariableInfo to;
    private TypeInfo typeInfo;

    /**
     * Instantiates a new New.
     *
     * @param to       the to
     * @param typeInfo the type info
     */
    public New(VariableInfo to, TypeInfo typeInfo) {
        this.to = to;
        this.typeInfo = typeInfo;
    }

    /**
     * Gets type info.
     *
     * @return the type info
     */
    public TypeInfo getTypeInfo() {
        return typeInfo;
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
