package com.squarebit.machinations.engine;

public class Addition extends BinaryOperator {
    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int evaluate() {
        return this.lhs.evaluate() + this.rhs.evaluate();
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
