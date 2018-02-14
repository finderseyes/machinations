package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.BooleanExpression;

public class Activator {
    private AbstractNode owner;
    private AbstractNode target;
    private String label;
    private BooleanExpression conditionExpression;

    public AbstractNode getOwner() {
        return owner;
    }

    public Activator setOwner(AbstractNode owner) {
        this.owner = owner;
        return this;
    }

    public AbstractNode getTarget() {
        return target;
    }

    public Activator setTarget(AbstractNode target) {
        this.target = target;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Activator setLabel(String label) {
        this.label = label;
        return this;
    }

    public BooleanExpression getConditionExpression() {
        return conditionExpression;
    }

    public Activator setConditionExpression(BooleanExpression conditionExpression) {
        this.conditionExpression = conditionExpression;
        return this;
    }

    public boolean evaluate() {
        return this.conditionExpression.evaluate();
    }
}
