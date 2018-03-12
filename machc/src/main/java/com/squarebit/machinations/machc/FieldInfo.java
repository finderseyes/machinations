package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.GGraphField;

/**
 * Information about a TField during compilation.
 */
final class FieldInfo extends SymbolInfo {
    private TypeInfo type;

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
}
