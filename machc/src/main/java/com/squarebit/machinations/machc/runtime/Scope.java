package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.runtime.components.TObject;

/**
 * The scope.
 */
public abstract class Scope {
    Scope parent;
    TObject[] variableTable;

    /**
     * Gets parent scope.
     *
     * @return the parent scope
     */
    public Scope getParent() {
        return parent;
    }
}
