package com.squarebit.machinations.models;

/**
 * Base class for modifiers.
 */
public abstract class Modifier extends Element {
    private Node owner;
    private GraphElement target;

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public Node getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     * @return the owner
     */
    public Modifier setOwner(Node owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public GraphElement getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target the target
     * @return the target
     */
    public Modifier setTarget(GraphElement target) {
        this.target = target;
        return this;
    }
}
