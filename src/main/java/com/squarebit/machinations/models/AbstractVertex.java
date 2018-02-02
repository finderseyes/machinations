package com.squarebit.machinations.models;

import java.util.HashSet;

public abstract class AbstractVertex {
    private HashSet<AbstractEdge> edges = new HashSet<>();

    /**
     * Instantiates a new Abstract vertex.
     */
    public AbstractVertex() {

    }
}
