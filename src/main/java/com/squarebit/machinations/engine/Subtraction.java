package com.squarebit.machinations.engine;

public class Subtraction extends BinaryOperator {
    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int evaluate() {
        return this.lhs.evaluate() - this.rhs.evaluate();
    }

    /**
     * Evaluate as probable and return probability.
     *
     * @return probability
     */
    @Override
    public float nonZeroProbability() {
        return this.lhs.nonZeroProbability() - this.rhs.nonZeroProbability();
    }

    /**
     * Of subtraction.
     *
     * @param lhs the lhs
     * @param rhs the rhs
     * @return the subtraction
     */
    public static Subtraction of(ArithmeticExpression lhs, ArithmeticExpression rhs) {
        return (Subtraction)new Subtraction().setLhs(lhs).setRhs(rhs);
    }
}
