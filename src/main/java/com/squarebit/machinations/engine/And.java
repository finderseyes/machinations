package com.squarebit.machinations.engine;

public class And extends BinaryLogicalOperator {
    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean evaluate() {
        return this.lhs.evaluate() && this.rhs.evaluate();
    }

    /**
     * Of and.
     *
     * @param lhs the lhs
     * @param rhs the rhs
     * @return the and
     */
    public static And of(LogicalExpression lhs, LogicalExpression rhs) {
        return (And)(new And().setLhs(lhs).setRhs(rhs));
    }
}
