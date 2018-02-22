package com.squarebit.machinations.engine;

public abstract class ArithmeticExpression extends Expression {
    /**
     * Evaluates the expression and returns its result.
     * @return integer result.
     */
    public abstract int evaluate();

    /**
     * Gets the probability that this expression will evaluate to a non-zero value.
     * @return non-zero probability
     */
    public abstract float nonZeroProbability();
}
