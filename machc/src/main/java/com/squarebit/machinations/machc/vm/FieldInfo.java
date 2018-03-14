package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.ast.GGraphField;

/**
 * Information about a TField during compilation.
 */
public final class FieldInfo extends SymbolInfo {
    private TypeInfo type;
    private int index;

    /**
     * Instantiates a new Field info.
     *
     * @param type the type
     */
    public FieldInfo(TypeInfo type) {
        this.type = type;
    }

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GGraphField getDeclaration() {
        return (GGraphField)this.declaration;
    }

    /**
     * Sets declaration.
     *
     * @param declaration the declaration
     * @return the declaration
     */
    public SymbolInfo setDeclaration(GGraphField declaration) {
        this.declaration = declaration;
        return this;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public TypeInfo getType() {
        return type;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index the index
     * @return the index
     */
    public FieldInfo setIndex(int index) {
        this.index = index;
        return this;
    }

    /**
     * Get field.
     *
     * @param instance the instance
     * @return the t object
     */
    public TObject get(TObject instance) {
        return instance.__fields__[index];
    }

    /**
     * Set.
     *
     * @param instance the instance
     * @param value    the value
     */
    public void set(TObject instance, TObject value) {
        instance.__fields__[index] = value;
    }
}
