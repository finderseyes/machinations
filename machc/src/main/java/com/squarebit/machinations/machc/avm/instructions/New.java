package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

/**
 * Instantiates a new object of given type.
 */
public final class New extends Instruction {
    private static final VariableInfo[] EMPTY_ARGS = new VariableInfo[0];
    private VariableInfo to;
    private TypeInfo typeInfo;
    private VariableInfo[] args;

    /**
     * Instantiates a new New.
     *
     * @param to       the to
     * @param typeInfo the type info
     */
    public New(VariableInfo to, TypeInfo typeInfo) {
        this(to, typeInfo, EMPTY_ARGS);
    }

    /**
     * Instantiates a new New.
     *
     * @param to       the to
     * @param typeInfo the type info
     * @param args     the args
     */
    public New(VariableInfo to, TypeInfo typeInfo, VariableInfo[] args) {
        this.to = to;
        this.typeInfo = typeInfo;
        this.args = args;
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

    /**
     * Get args variable info [ ].
     *
     * @return the variable info [ ]
     */
    public VariableInfo[] getArgs() {
        return args;
    }
}
