package com.squarebit.machinations.machc.ast.statements;

import com.squarebit.machinations.machc.ast.GStatement;
import com.squarebit.machinations.machc.ast.expressions.GExpression;

/**
 * Return statement.
 */
public class GReturn extends GStatement {
    private final GExpression expression;

    /**
     * Instantiates a new G return.
     */
    public GReturn() {
        this.expression = null;
    }

    /**
     * Instantiates a new G return.
     *
     * @param expression the expression
     */
    public GReturn(GExpression expression) {
        this.expression = expression;
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public GExpression getExpression() {
        return expression;
    }
}
