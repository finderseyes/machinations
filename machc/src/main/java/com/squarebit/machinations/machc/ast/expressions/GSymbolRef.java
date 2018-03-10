package com.squarebit.machinations.machc.ast.expressions;

/**
 * Reference to a symbol.
 */
public final class GSymbolRef extends GExpression {
    private final String symbolName;

    /**
     * Instantiates symbol reference.
     *
     * @param symbolName the symbol name
     */
    public GSymbolRef(String symbolName) {
        this.symbolName = symbolName;
    }

    /**
     * Gets the referenced symbol name.
     *
     * @return the symbol name
     */
    public String getSymbolName() {
        return symbolName;
    }
}
