package com.squarebit.machinations.machc.ast.statements;

import com.squarebit.machinations.machc.ast.GStatement;
import com.squarebit.machinations.machc.ast.expressions.GExpression;

/**
 * If then else.
 */
public class GIfThenElse extends GStatement {
    private GExpression condition;
    private GStatement whenTrue;
    private GStatement whenFalse;

    /**
     * Instantiates a new G if then else.
     *
     * @param condition the condition
     * @param whenTrue  the when true
     */
    public GIfThenElse(GExpression condition, GStatement whenTrue) {
        this.condition = condition;
        this.whenTrue = whenTrue;
    }

    /**
     * Instantiates a new G if then else.
     *
     * @param condition the condition
     * @param whenTrue  the when true
     * @param whenFalse the when false
     */
    public GIfThenElse(GExpression condition, GStatement whenTrue, GStatement whenFalse) {
        this.condition = condition;
        this.whenTrue = whenTrue;
        this.whenFalse = whenFalse;
    }

    public GExpression getCondition() {
        return condition;
    }

    public GStatement getWhenTrue() {
        return whenTrue;
    }

    public GStatement getWhenFalse() {
        return whenFalse;
    }
}
