package com.squarebit.machinations.engine;

public class Addition extends BinaryOperator {
    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int eval() {
        return this.lhs.eval() + this.rhs.eval();
    }

    /**
     * Of addition.
     *
     * @param lhs the lhs
     * @param rhs the rhs
     * @return the addition
     */
    public static Addition of(ArithmeticExpression lhs, ArithmeticExpression rhs) {
        return (Addition) new Addition().setLhs(lhs).setRhs(rhs);
    }
}
