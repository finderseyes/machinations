package com.squarebit.machinations.machc.ast;

import java.util.HashMap;
import java.util.Map;

/**
 * A Mach code unit, which corresponds to a source code file.
 */
public final class GUnit {
    private Map<String, GGraph> graphs = new HashMap<>(); // Mapping from graph id to graph instance.

    public GGraph findGraph(String id) {
        return graphs.get(id);
    }

    public GGraph addGraph(GGraph graph) {
        return graphs.put(graph.getId(), graph);
    }
}
