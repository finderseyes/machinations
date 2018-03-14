package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.ast.GSymbol;

/**
 * A symbol.
 */
public abstract class SymbolInfo {
    GSymbol declaration;
    String name;

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GSymbol getDeclaration() {
        return declaration;
    }

    /**
     * Sets declaration.
     *
     * @param declaration the declaration
     * @return the declaration
     */
    public SymbolInfo setDeclaration(GSymbol declaration) {
        this.declaration = declaration;
        return this;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     */
    public SymbolInfo setName(String name) {
        this.name = name;
        return this;
    }
}
