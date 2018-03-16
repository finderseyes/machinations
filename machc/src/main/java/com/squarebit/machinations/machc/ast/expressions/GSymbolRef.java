package com.squarebit.machinations.machc.ast.expressions;

/**
 * Reference to a symbol.
 */
public final class GSymbolRef extends GExpression {
    private final String symbolName;
    private GSymbolRef next;

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

    /**
     * Gets next.
     *
     * @return the next
     */
    public GSymbolRef getNext() {
        return next;
    }

    /**
     * Sets next.
     *
     * @param next the next
     * @return the next
     */
    public GSymbolRef setNext(GSymbolRef next) {
        this.next = next;
        return this;
    }
}
