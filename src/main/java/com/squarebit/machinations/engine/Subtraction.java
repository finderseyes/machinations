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
    public float evaluateAsProbable() {
        return this.lhs.evaluateAsProbable() - this.rhs.evaluateAsProbable();
    }

    @Override
    public float evaluate(EvaluationContext context) {
        return this.lhs.evaluate(context) - this.rhs.evaluate(context);
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
