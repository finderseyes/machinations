package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.runtime.components.TObject;

/**
 * The scope.
 */
public abstract class Scope {
    private Scope parent;

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Scope setParent(Scope parent) {
        this.parent = parent;
        return this;
    }

    public Scope getParent() {
        return parent;
    }

    public final Object findSymbol(String name) {
        Object result = findLocalSymbol(name);

        if (result == null && parent != null)
            return parent.findSymbol(name);
        return result;
    }

    protected abstract Object findLocalSymbol(String name);
}
