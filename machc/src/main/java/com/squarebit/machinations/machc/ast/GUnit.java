package com.squarebit.machinations.machc.ast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A Mach code unit, which corresponds to a source code file.
 */
public final class GUnit extends GObject {
    private Map<String, GGraph> graphs = new HashMap<>(); // Mapping from graph id to graph instance.

    /**
     * Finds a graph with given id.
     *
     * @param id the id
     * @return the graph
     */
    public GGraph findGraph(String id) {
        return graphs.get(id);
    }

    /**
     * Adds a graph.
     *
     * @param graph the graph
     * @return the graph
     */
    public GGraph addGraph(GGraph graph) {
        return graphs.put(graph.getId(), graph);
    }

    /**
     * Gets graphs declared in this unit.
     *
     * @return the graphs declared in this unit.
     */
    public Collection<GGraph> getGraphs() {
        return graphs.values();
    }
}
