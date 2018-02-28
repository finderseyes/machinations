package com.squarebit.machinations.models;

public class NodeEvaluationContext {
    private Object requester;
    private Object expression;

    public Object getRequester() {
        return requester;
    }

    public NodeEvaluationContext setRequester(Object requester) {
        this.requester = requester;
        return this;
    }

    public Object getExpression() {
        return expression;
    }

    public NodeEvaluationContext setExpression(Object expression) {
        this.expression = expression;
        return this;
    }
}
