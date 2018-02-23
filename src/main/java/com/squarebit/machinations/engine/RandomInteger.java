package com.squarebit.machinations.engine;

import org.apache.commons.lang3.RandomUtils;

/**
 * A random flow rate by dice rolling.
 */
public class RandomInteger extends IntegerExpression {
    private int times = 1;
    private int faces = 6;

    /**
     * Determines if the expression evaluates to a random value.
     *
     * @return true if the expression value is random, false otherwise
     */
    @Override
    public boolean isRandom() {
        return (faces > 1);
    }

    /**
     * Evaluates the flow rate.
     *
     * @return integer value of the flow rate.
     */
    @Override
    public int eval() {
        int sum = 0;
        for (int i = 0; i < this.times; i++)
            sum += RandomUtils.nextInt(1, this.faces + 1);
        return sum;
    }

    /**
     * Creates a new random flow rate instance.
     *
     * @param times the number of rolling times
     * @param faces the number of faces of the dice
     * @return the random flow rate instance
     */
    public static RandomInteger of(int times, int faces) {
        RandomInteger randomFlowRate = new RandomInteger();
        randomFlowRate.times = times;
        randomFlowRate.faces = faces;
        return randomFlowRate;
    }
}
