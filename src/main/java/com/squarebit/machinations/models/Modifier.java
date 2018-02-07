package com.squarebit.machinations.models;

/**
 * (label/node modifier).
 */
public class Modifier {
    private AbstractNode owner;
    private Object target;

    public AbstractNode getOwner() {
        return owner;
    }

    public Modifier setOwner(AbstractNode owner) {
        this.owner = owner;
        return this;
    }

    public Object getTarget() {
        return target;
    }

    public Modifier setTarget(Object target) {
        this.target = target;
        return this;
    }
}
