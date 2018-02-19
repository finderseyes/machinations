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
     * Evaluate as probable and return probability.
     *
     * @return probability
     */
    @Override
    public float evaluateAsProbable() {
        return this.lhs.evaluateAsProbable() + this.rhs.evaluateAsProbable();
    }

    @Override
    public float evaluate(EvaluationContext context) {
        return this.lhs.evaluate(context) + this.rhs.evaluate(context);
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
