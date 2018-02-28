package com.squarebit.machinations.engine;

import org.apache.commons.lang3.RandomUtils;

/**
 * A random flow rate by dice rolling.
 */
public class RandomInteger extends IntegerExpression {
    private static final String DRAW_PREFIX = "draw";
    private static final int DRAW_PREFIX_LENGTH = DRAW_PREFIX.length();

    private int times = 1;
    private int faces = 6;

    /**
     * Gets times.
     *
     * @return the times
     */
    public int getTimes() {
        return times;
    }

    /**
     * Gets faces.
     *
     * @return the faces
     */
    public int getFaces() {
        return faces;
    }

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
     * Evaluates the expression to universal numerical type (float).
     *
     * @return value as float
     */
    @Override
    public float evalAsFloat() {
        return this.eval();
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

    /**
     * Parse a random integer from a string.
     *
     * @param text the string
     * @return the random integer
     */
    public static RandomInteger parse(String text) {
        if (text.startsWith(DRAW_PREFIX)) {
            return RandomInteger.of(1, Integer.parseInt(text.substring(DRAW_PREFIX_LENGTH)));
        }
        else {
            if (text.equals("D"))
                return RandomInteger.of(1, 6);
            else if (text.charAt(0) == 'D')
                return RandomInteger.of(1, Integer.parseInt(text.substring(1)));
            else if (text.charAt(text.length() - 1) == 'D')
                return RandomInteger.of(Integer.parseInt(text.substring(0, text.length() - 1)), 6);
            else {
                String[] parts = text.split("D");
                return RandomInteger.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }
        }
    }
}
