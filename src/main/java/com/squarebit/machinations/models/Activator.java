package com.squarebit.machinations.models;

/**
 * Trigger/activator.
 */
public class Activator {
    private AbstractNode owner;
    private AbstractNode target;

    public AbstractNode getOwner() {
        return owner;
    }

    public Activator setOwner(AbstractNode owner) {
        this.owner = owner;
        return this;
    }

    public Object getTarget() {
        return target;
    }

    public Activator setTarget(AbstractNode target) {
        this.target = target;
        return this;
    }
}
