package com.squarebit.machinations.machc.avm;

/**
 * A local variable in a {@link Scope}.
 */
public final class VariableInfo implements Symbol {
    private Scope declaringScope;
    private String name;
    private TypeInfo type;
    private int index;

    /**
     * Instantiates a new instance.
     */
    public VariableInfo() {
    }

    public Scope getDeclaringScope() {
        return declaringScope;
    }

    public VariableInfo setDeclaringScope(Scope declaringScope) {
        this.declaringScope = declaringScope;
        return this;
    }

    public String getName() {
        return name;
    }

    public VariableInfo setName(String name) {
        this.name = name;
        return this;
    }

    public TypeInfo getType() {
        return type;
    }

    public VariableInfo setType(TypeInfo type) {
        this.type = type;
        return this;
    }

    /**
     * Gets index (offset) of the variable in the call frame.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets variable index (offset) in the call frame.
     *
     * @param index the index
     * @return the index
     */
    public VariableInfo setIndex(int index) {
        this.index = index;
        return this;
    }
}
