package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.runtime.components.TObject;

/**
 * The scope.
 */
public abstract class Scope {
    private Scope parent;

    /**
     * Instantiates a new scope.
     *
     * @param parent the parent
     */
    protected Scope(Scope parent) {
        this.parent = parent;
    }

    /**
     * Sets parent.
     *
     * @param parent the parent
     * @return the scope
     */
    public Scope setParent(Scope parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Gets parent.
     *
     * @return the parent
     */
    public Scope getParent() {
        return parent;
    }

    /**
     * Find the closest symbol with given name in this scope or its parents.
     *
     * @param name the symbol name
     * @return the symbol or null
     */
    public final Object findSymbol(String name) {
        Object result = findLocalSymbol(name);

        if (result == null && parent != null)
            return parent.findSymbol(name);
        return result;
    }

    /**
     * Finds a symbol with given name within this scope.
     *
     * @param name the synmbol name
     * @return the symbol or null.
     */
    protected abstract Object findLocalSymbol(String name);
}
