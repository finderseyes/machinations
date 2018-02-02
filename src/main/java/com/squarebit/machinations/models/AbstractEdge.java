package com.squarebit.machinations.models;

import lombok.Data;

@Data
public abstract class AbstractEdge {
    private AbstractVertex from;
    private AbstractVertex to;

    /**
     * Instantiates a new Abstract edge.
     */
    public AbstractEdge() {
        this.from = this.to = null;
    }

    public AbstractEdge(AbstractVertex from, AbstractVertex to) {
        this.from = from;
        this.to = to;
    }
}
