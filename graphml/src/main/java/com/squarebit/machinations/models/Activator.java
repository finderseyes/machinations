package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.LogicalExpression;

public class Activator extends Element {
    private Node owner;
    private Node target;
    private LogicalExpression condition;

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
    public Activator setOwner(Node owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public Node getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target the target
     * @return the target
     */
    public Activator setTarget(Node target) {
        this.target = target;
        return this;
    }

    /**
     * Gets condition.
     *
     * @return the condition
     */
    public LogicalExpression getCondition() {
        return condition;
    }

    /**
     * Sets condition.
     *
     * @param condition the condition
     * @return the condition
     */
    public Activator setCondition(LogicalExpression condition) {
        this.condition = condition;
        return this;
    }

    public boolean evaluate() {
        return this.condition.eval();
    }
}
