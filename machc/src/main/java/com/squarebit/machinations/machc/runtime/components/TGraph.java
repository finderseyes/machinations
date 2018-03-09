package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.runtime.components.annotations.NativeMethod;

public abstract class TGraph extends TObject {
    /**
     * Determines if the graph has reached an end node.
     * @return true or false
     */
    @NativeMethod
    public abstract boolean isTerminated();
}
