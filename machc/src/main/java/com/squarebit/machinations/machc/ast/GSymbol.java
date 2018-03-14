package com.squarebit.machinations.machc.ast;

/**
 * Base class for any declaration (graphs, fields, methods, variables, etc.).
 */
public abstract class GSymbol {
    private String name;

    /**
     * Gets the symbol name.
     *
     * @return the symbol name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the symbol name.
     *
     * @param name the symbol name
     * @return the symbol
     */
    public GSymbol setName(String name) {
        this.name = name;
        return this;
    }
}
