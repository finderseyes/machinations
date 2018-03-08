package com.squarebit.machinations.machc.runtime.components;

public abstract class TGraph extends TObject {
    /**
     * Determines if the graph has reached an end node.
     * @return true or false
     */
    public abstract TBoolean isTerminated();
}
