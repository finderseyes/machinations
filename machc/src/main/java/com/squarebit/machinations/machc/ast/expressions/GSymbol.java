package com.squarebit.machinations.machc.ast.expressions;

/**
 * Reference to a local variable/this object field/global name.
 */
public class GSymbol extends GExpression implements GAssignmentTarget {
    private String symbolName;

    public String getSymbolName() {
        return symbolName;
    }

    public GSymbol setSymbolName(String symbolName) {
        this.symbolName = symbolName;
        return this;
    }
}
