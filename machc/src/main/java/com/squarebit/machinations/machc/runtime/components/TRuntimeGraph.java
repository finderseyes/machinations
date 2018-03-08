package com.squarebit.machinations.machc.runtime.components;

public class TRuntimeGraph extends TGraph {
    /**
     * Determines if the graph has reached an end node.
     *
     * @return true or false
     */
    @Override
    public TBoolean isTerminated() {
        return new TBoolean(false);
    }
}
