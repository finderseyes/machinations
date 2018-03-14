package com.squarebit.machinations.machc.ast.expressions;

public final class GSuffixExpression extends GExpression {
    private String operator;
    private GExpression child;

    public String getOperator() {
        return operator;
    }

    public GSuffixExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public GExpression getChild() {
        return child;
    }

    public GSuffixExpression setChild(GExpression child) {
        this.child = child;
        return this;
    }
}
