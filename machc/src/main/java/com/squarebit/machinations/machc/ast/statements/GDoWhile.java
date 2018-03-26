package com.squarebit.machinations.machc.ast.statements;

import com.squarebit.machinations.machc.ast.GStatement;
import com.squarebit.machinations.machc.ast.expressions.GExpression;

/**
 * The type G do while.
 */
public class GDoWhile extends GStatement {
    private GExpression condition;
    private GStatement statement;

    /**
     * Gets condition.
     *
     * @return the condition
     */
    public GExpression getCondition() {
        return condition;
    }

    /**
     * Sets condition.
     *
     * @param condition the condition
     * @return the condition
     */
    public GDoWhile setCondition(GExpression condition) {
        this.condition = condition;
        return this;
    }

    /**
     * Gets statement.
     *
     * @return the statement
     */
    public GStatement getStatement() {
        return statement;
    }

    /**
     * Sets statement.
     *
     * @param statement the statement
     * @return the statement
     */
    public GDoWhile setStatement(GStatement statement) {
        this.statement = statement;
        return this;
    }
}
