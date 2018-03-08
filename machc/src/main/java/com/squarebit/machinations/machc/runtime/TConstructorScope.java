package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.runtime.components.TObject;

public class TConstructorScope extends Scope {
    TObject thisObject;

    /**
     * Gets the object reference corresponding to "this" object.
     *
     * @return the "this" object reference
     */
    public TObject getThisObject() {
        return thisObject;
    }

    /**
     * Sets "this" object reference.
     *
     * @param thisObject the "this" object
     * @return the "this" object reference
     */
    public TConstructorScope setThisObject(TObject thisObject) {
        this.thisObject = thisObject;
        return this;
    }
}
