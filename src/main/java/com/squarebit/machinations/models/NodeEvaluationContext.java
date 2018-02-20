package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.Expression;

public class NodeEvaluationContext {
    private Object owner;
    private Expression expression;

    public Object getOwner() {
        return owner;
    }

    public NodeEvaluationContext setOwner(Object owner) {
        this.owner = owner;
        return this;
    }

    public Expression getExpression() {
        return expression;
    }

    public NodeEvaluationContext setExpression(Expression expression) {
        this.expression = expression;
        return this;
    }
}
