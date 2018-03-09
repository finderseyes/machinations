package com.squarebit.machinations.machc.runtime.components;

public class TRuntimeGraph extends TGraph {
    /**
     * Determines if the graph has reached an end node.
     *
     * @return true or false
     */
    @Override
    public boolean isTerminated() {
        return false;
    }
}
