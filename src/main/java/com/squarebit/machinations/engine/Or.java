package com.squarebit.machinations.engine;

public class Or extends BinaryLogicalOperator {
    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean evaluate() {
        return this.lhs.evaluate() || this.rhs.evaluate();
    }

    public static Or of(LogicalExpression lhs, LogicalExpression rhs) {
        return (Or)(new Or().setLhs(lhs).setRhs(rhs));
    }
}
