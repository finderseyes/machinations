package com.squarebit.machinations.engine;

public abstract class ArithmeticExpression extends Expression {
    @Override
    public DataType getType() {
        return DataType.INTEGER;
    }

    /**
     * Evaluates the expression and returns its result.
     * @return integer result.
     */
    public abstract int evaluate();

    /**
     * Evaluate as probable and return probability.
     * @return probability
     */
    public abstract float evaluateAsProbable();

    /**
     * Evaluate int.
     *
     * @param context the context
     * @return the int
     */
    public float evaluate(EvaluationContext context) {
        return evaluate();
    }
}
