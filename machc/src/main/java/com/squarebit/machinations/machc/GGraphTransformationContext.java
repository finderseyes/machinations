package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.GGraph;

/**
 * The graph transformation context.
 */
final class GGraphTransformationContext {
    private GUnitTransformationContext unitTransformationContext;
    private GGraph graph;

    /**
     * Gets unit transformation context.
     *
     * @return the unit transformation context
     */
    public GUnitTransformationContext getUnitTransformationContext() {
        return unitTransformationContext;
    }

    /**
     * Sets unit transformation context.
     *
     * @param unitTransformationContext the unit transformation context
     * @return the unit transformation context
     */
    public GGraphTransformationContext setUnitTransformationContext(GUnitTransformationContext unitTransformationContext) {
        this.unitTransformationContext = unitTransformationContext;
        return this;
    }

    /**
     * Gets graph.
     *
     * @return the graph
     */
    public GGraph getGraph() {
        return graph;
    }

    /**
     * Sets graph.
     *
     * @param graph the graph
     * @return the graph
     */
    public GGraphTransformationContext setGraph(GGraph graph) {
        this.graph = graph;
        return this;
    }
}
