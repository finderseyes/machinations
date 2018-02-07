package com.squarebit.machinations.models;

public abstract class AbstractConnection extends AbstractElement {
    private AbstractNode from;
    private AbstractNode to;

    /**
     * Instantiates a new Abstract edge.
     */
    public AbstractConnection() {
        this.from = this.to = null;
    }

    /**
     * Instantiates a new Abstract connection.
     *
     * @param from the from
     * @param to   the to
     */
    public AbstractConnection(AbstractNode from, AbstractNode to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public AbstractNode getFrom() {
        return from;
    }

    /**
     * Sets from.
     *
     * @param from the from
     * @return the from
     */
    public AbstractConnection setFrom(AbstractNode from) {
        this.from = from;
        return this;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public AbstractNode getTo() {
        return to;
    }

    /**
     * Sets to.
     *
     * @param to the to
     * @return the to
     */
    public AbstractConnection setTo(AbstractNode to) {
        this.to = to;
        return this;
    }
}
