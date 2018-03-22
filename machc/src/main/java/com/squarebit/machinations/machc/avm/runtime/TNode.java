package com.squarebit.machinations.machc.avm.runtime;

/**
 * Base class of all nodes.
 */
public abstract class TNode implements TObject {
    TGraph graph;

    /**
     * Gets the graph containing this node.
     *
     * @return the graph
     */
    public TGraph getGraph() {
        return graph;
    }
}
