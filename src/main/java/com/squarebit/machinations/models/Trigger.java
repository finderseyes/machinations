package com.squarebit.machinations.models;

public class Trigger {
    private AbstractNode owner;
    private Object target;

    public AbstractNode getOwner() {
        return owner;
    }

    public Trigger setOwner(AbstractNode owner) {
        this.owner = owner;
        return this;
    }

    public Object getTarget() {
        return target;
    }

    public Trigger setTarget(Object target) {
        this.target = target;
        return this;
    }
}
