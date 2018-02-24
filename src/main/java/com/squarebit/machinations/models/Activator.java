package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.LogicalExpression;

public class Activator {
    private Node owner;
    private Node target;
    private String label;
    private LogicalExpression conditionExpression;

    public Node getOwner() {
        return owner;
    }

    public Activator setOwner(Node owner) {
        this.owner = owner;
        return this;
    }

    public Node getTarget() {
        return target;
    }

    public Activator setTarget(Node target) {
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

    public LogicalExpression getConditionExpression() {
        return conditionExpression;
    }

    public Activator setConditionExpression(LogicalExpression conditionExpression) {
        this.conditionExpression = conditionExpression;
        return this;
    }

    public boolean evaluate() {
        return this.conditionExpression.eval();
    }
}
