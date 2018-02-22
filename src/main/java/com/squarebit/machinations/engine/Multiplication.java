package com.squarebit.machinations.engine;

public class Multiplication extends BinaryOperator {
    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int evaluate() {
        return this.lhs.evaluate() * this.rhs.evaluate();
    }

    /**
     * Evaluate as probable and return probability.
     *
     * @return probability
     */
    @Override
    public float nonZeroProbability() {
        return this.lhs.nonZeroProbability() * this.rhs.nonZeroProbability();
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
