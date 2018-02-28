package com.squarebit.machinations.engine;

public class Multiplication extends BinaryOperator {
    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int eval() {
        return this.lhs.eval() * this.rhs.eval();
    }

    /**
     * Of multiplication.
     *
     * @param lhs the lhs
     * @param rhs the rhs
     * @return the multiplication
     */
    public static Multiplication of(ArithmeticExpression lhs, ArithmeticExpression rhs) {
        return (Multiplication)(new Multiplication().setLhs(lhs).setRhs(rhs));
    }
}
