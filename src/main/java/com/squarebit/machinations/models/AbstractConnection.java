package com.squarebit.machinations.models;

import lombok.Data;

@Data
public abstract class AbstractConnection {
    private AbstractNode from;
    private AbstractNode to;

    /**
     * Instantiates a new Abstract edge.
     */
    public AbstractConnection() {
        this.from = this.to = null;
    }

    public AbstractConnection(AbstractNode from, AbstractNode to) {
        this.from = from;
        this.to = to;
    }
}
