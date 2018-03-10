package com.squarebit.machinations.machc.ast.expressions;

public final class GPrefixExpression extends GExpression {
    private String operator;
    private GExpression child;

    public String getOperator() {
        return operator;
    }

    public GPrefixExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public GExpression getChild() {
        return child;
    }

    public GPrefixExpression setChild(GExpression child) {
        this.child = child;
        return this;
    }
}
