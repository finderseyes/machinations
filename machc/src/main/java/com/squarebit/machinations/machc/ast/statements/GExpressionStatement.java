package com.squarebit.machinations.machc.ast.statements;

import com.squarebit.machinations.machc.ast.GStatement;
import com.squarebit.machinations.machc.ast.expressions.GExpression;

public class GExpressionStatement extends GStatement {
    private GExpression expression;

    public GExpression getExpression() {
        return expression;
    }

    public GExpressionStatement setExpression(GExpression expression) {
        this.expression = expression;
        return this;
    }
}
