package com.squarebit.machinations.machc.vm.expressions;

import com.squarebit.machinations.machc.vm.components.TObjectBase;

public final class ObjectRef {
    private final TObjectBase ref;

    /**
     * Instantiates a new Object ref.
     *
     * @param ref the ref
     */
    public ObjectRef(TObjectBase ref) {
        this.ref = ref;
    }

    /**
     * Gets ref.
     *
     * @return the ref
     */
    public TObjectBase getRef() {
        return ref;
    }
}
