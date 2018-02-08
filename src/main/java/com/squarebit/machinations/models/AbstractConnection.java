package com.squarebit.machinations.models;

public abstract class AbstractConnection extends AbstractElement {
    private AbstractNode from;
    private AbstractNode to;
    private String label;

    public AbstractNode getFrom() {
        return from;
    }

    public AbstractConnection setFrom(AbstractNode from) {
        this.from = from;
        return this;
    }

    public AbstractNode getTo() {
        return to;
    }

    public AbstractConnection setTo(AbstractNode to) {
        this.to = to;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public AbstractConnection setLabel(String label) {
        this.label = label;
        return this;
    }
}
