package com.squarebit.machinations.engine;

import org.apache.commons.lang3.RandomUtils;

/**
 * Percentage.
 */
public class ProbableNumber extends ArithmeticExpression {
    private float probability = 1.0f;

    /**
     *
     */
    public ProbableNumber() {
    }

    /**
     *
     * @param probability
     */
    public ProbableNumber(float probability) {
        this.probability = probability;
    }

    /**
     * Gets probability.
     *
     * @return the probability
     */
    public float getProbability() {
        return probability;
    }

    /**
     * Sets probability.
     *
     * @param probability the probability
     * @return the probability
     */
    public ProbableNumber setProbability(float probability) {
        this.probability = probability;
        return this;
    }

    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int evaluate() {
        float rand = RandomUtils.nextFloat(0.0f, 1.0f);
        return rand <= probability ? 1 : 0;
    }

    /**
     * Of probable number.
     *
     * @param probability the probability
     * @return the probable number
     */
    public static ProbableNumber of(float probability) {
        return (ProbableNumber)(new ProbableNumber().setProbability(probability));
    }
}
