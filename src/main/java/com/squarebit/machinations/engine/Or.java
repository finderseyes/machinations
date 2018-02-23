package com.squarebit.machinations.engine;

public class Or extends BinaryLogicalOperator {
    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean eval() {
        return this.lhs.eval() || this.rhs.eval();
    }

    public static Or of(LogicalExpression lhs, LogicalExpression rhs) {
        return (Or)(new Or().setLhs(lhs).setRhs(rhs));
    }
}
