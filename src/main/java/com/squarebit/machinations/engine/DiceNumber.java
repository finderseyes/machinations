package com.squarebit.machinations.engine;

import org.apache.commons.lang3.RandomUtils;

public class DiceNumber extends ArithmeticExpression {
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
     * Sets times.
     *
     * @param times the times
     * @return the times
     */
    public DiceNumber setTimes(int times) {
        this.times = times;
        return this;
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
     * Sets faces.
     *
     * @param faces the faces
     * @return the faces
     */
    public DiceNumber setFaces(int faces) {
        this.faces = faces;
        return this;
    }

    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int evaluate() {
        int sum = 0;
        for (int i = 0; i < this.times; i++)
            sum += RandomUtils.nextInt(1, this.faces + 1);
        return sum;
    }

    public static DiceNumber of(int times, int faces) {
        return new DiceNumber().setTimes(times).setFaces(faces);
    }
}
