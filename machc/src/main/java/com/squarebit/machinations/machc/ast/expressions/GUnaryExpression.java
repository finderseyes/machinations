package com.squarebit.machinations.machc.ast.expressions;

/**
 *
 */
public final class GUnaryExpression extends GExpression {
    private String operator;
    private GExpression child;

    public String getOperator() {
        return operator;
    }

    public GUnaryExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public GExpression getChild() {
        return child;
    }

    public GUnaryExpression setChild(GExpression child) {
        this.child = child;
        return this;
    }
}
