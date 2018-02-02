package com.squarebit.machinations.models;

import com.google.common.collect.ImmutableSet;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.HashSet;
import java.util.Set;

public class MachinationsContext {
    private HashSet<AbstractVertex> vertices;
    private HashSet<AbstractEdge> edges;
    private HashSet<AbstractVertex> startVertices;

    /**
     * Instantiates a new Machinations context.
     */
    private MachinationsContext() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
        this.startVertices = new HashSet<>();
    }

    public Set<AbstractVertex> getVertices() {
        return ImmutableSet.copyOf(this.vertices);
    }

    public Set<AbstractEdge> getEdges() {
        return this.edges;
    }

    public static MachinationsContext fromSpecs(TinkerGraph specs) {
        MachinationsContext context = new MachinationsContext();

        // Iterate all edges.
        specs.vertices().forEachRemaining(v -> {
            int k = 10;
        });

        // Iterate all edges.
        specs.edges().forEachRemaining(e -> {
            int k = 10;
        });

        return context;
    }

}
