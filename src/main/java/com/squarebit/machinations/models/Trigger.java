package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.Expression;

public class Trigger {
    private Node owner;
    private Element target;
    private Expression labelExpression;

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
    public Trigger setOwner(Node owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public Element getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target the target
     * @return the target
     */
    public Trigger setTarget(Element target) {
        this.target = target;
        return this;
    }

    /**
     * Gets label expression.
     *
     * @return the label expression
     */
    public Expression getLabelExpression() {
        return labelExpression;
    }

    /**
     * Sets label expression.
     *
     * @param labelExpression the label expression
     * @return the label expression
     */
    public Trigger setLabelExpression(Expression labelExpression) {
        this.labelExpression = labelExpression;
        return this;
    }
}
