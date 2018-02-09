package com.squarebit.machinations.models;

public class Trigger {
    private AbstractNode owner;
    private AbstractElement target;

    public AbstractNode getOwner() {
        return owner;
    }

    public Trigger setOwner(AbstractNode owner) {
        this.owner = owner;
        return this;
    }

    public AbstractElement getTarget() {
        return target;
    }

    public Trigger setTarget(AbstractElement target) {
        this.target = target;
        return this;
    }
}
