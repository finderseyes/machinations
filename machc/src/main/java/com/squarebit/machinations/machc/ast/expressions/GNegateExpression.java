package com.squarebit.machinations.machc.ast.expressions;

public final class GNegateExpression extends GExpression {
    private GExpression child;

    public GExpression getChild() {
        return child;
    }

    public GNegateExpression setChild(GExpression child) {
        this.child = child;
        return this;
    }
}
