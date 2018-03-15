package com.squarebit.machinations.machc.avm;

/**
 * A local variable in a {@link Scope}.
 */
public final class VariableInfo {
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

    public int getIndex() {
        return index;
    }

    public VariableInfo setIndex(int index) {
        this.index = index;
        return this;
    }
}
