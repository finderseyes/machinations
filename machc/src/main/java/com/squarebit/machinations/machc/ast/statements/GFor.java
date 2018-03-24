package com.squarebit.machinations.machc.ast.statements;

import com.squarebit.machinations.machc.ast.GStatement;
import com.squarebit.machinations.machc.ast.expressions.GExpression;

public class GFor extends GStatement {
    private GStatement init;
    private GExpression expression;
    private GStatement update;
    private GStatement statement;

    public GStatement getInit() {
        return init;
    }

    public GFor setInit(GStatement init) {
        this.init = init;
        return this;
    }

    public GExpression getExpression() {
        return expression;
    }

    public GFor setExpression(GExpression expression) {
        this.expression = expression;
        return this;
    }

    public GStatement getUpdate() {
        return update;
    }

    public GFor setUpdate(GStatement update) {
        this.update = update;
        return this;
    }

    public GStatement getStatement() {
        return statement;
    }

    public GFor setStatement(GStatement statement) {
        this.statement = statement;
        return this;
    }
}
