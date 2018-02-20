package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.Expression;

public class Trigger {
    private AbstractNode owner;
    private AbstractElement target;
    private Expression labelExpression;

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public AbstractNode getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     * @return the owner
     */
    public Trigger setOwner(AbstractNode owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public AbstractElement getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target the target
     * @return the target
     */
    public Trigger setTarget(AbstractElement target) {
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
