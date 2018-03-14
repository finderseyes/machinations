package com.squarebit.machinations.machc.ast;

import java.util.HashMap;
import java.util.Map;

/**
 * A scope, which contains symbol declarations.
 */
public final class GScope {
    private final GScope parent;
    private Map<String, GSymbol> symbols = new HashMap<>();

    /**
     * Instantiates a new G scope.
     */
    public GScope() {
        this.parent = null;
    }

    /**
     * Instantiates a new scope.
     *
     * @param parent the parent scope
     */
    public GScope(GScope parent) {
        this.parent = parent;
    }
}
