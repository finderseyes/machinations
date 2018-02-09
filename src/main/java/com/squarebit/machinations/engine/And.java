package com.squarebit.machinations.engine;

public class And extends BinaryBooleanOperator {
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
    public static And of(BooleanExpression lhs, BooleanExpression rhs) {
        return (And)(new And().setLhs(lhs).setRhs(rhs));
    }
}
