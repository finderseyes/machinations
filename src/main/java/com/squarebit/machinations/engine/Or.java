package com.squarebit.machinations.engine;

public class Or extends BinaryBooleanOperator {
    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean evaluate() {
        return this.lhs.evaluate() || this.rhs.evaluate();
    }

    public static Or of(BooleanExpression lhs, BooleanExpression rhs) {
        return (Or)(new Or().setLhs(lhs).setRhs(rhs));
    }
}
