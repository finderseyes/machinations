package com.squarebit.machinations.machc.runtime;

/**
 * The scope.
 */
public abstract class Scope {
    Scope parent;

    /**
     * Gets parent scope.
     *
     * @return the parent scope
     */
    public Scope getParent() {
        return parent;
    }
}
