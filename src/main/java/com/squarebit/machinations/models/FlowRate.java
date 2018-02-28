package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.*;
import org.apache.commons.lang3.RandomUtils;

/**
 * A flow rate.
 */
public class FlowRate {
    public static final FlowRate ALL = new FlowRate();
    public static final IntegerExpression DEFAULT_VALUE = FixedInteger.of(1.0f);
    public static final IntegerExpression DEFAULT_INTERVAL = FixedInteger.of(1.0f);
    public static final IntegerExpression DEFAULT_MULTIPLIER = FixedInteger.of(1.0f);
    public static final Percentage DEFAULT_PROBABILITY = Percentage.of(100);

    private IntegerExpression value = DEFAULT_VALUE;
    private IntegerExpression interval = DEFAULT_INTERVAL;
    private IntegerExpression multiplier = DEFAULT_MULTIPLIER;
    private Percentage probability = DEFAULT_PROBABILITY;

    private int currentInterval = 1;
    private int times = 1;


    /**
     * Gets value.
     *
     * @return the value
     */
    public IntegerExpression getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     * @return the value
     */
    public FlowRate setValue(IntegerExpression value) {
        this.value = value;
        return this;
    }

    /**
     * Gets interval.
     *
     * @return the interval
     */
    public IntegerExpression getInterval() {
        return interval;
    }

    /**
     * Sets interval.
     *
     * @param interval the interval
     * @return the interval
     */
    public FlowRate setInterval(IntegerExpression interval) {
        this.interval = interval;
        this.currentInterval = evaluateInterval();
        return this;
    }

    /**
     * Gets multiplier.
     *
     * @return the multiplier
     */
    public IntegerExpression getMultiplier() {
        return multiplier;
    }

    /**
     * Sets multiplier.
     *
     * @param multiplier the multiplier
     * @return the multiplier
     */
    public FlowRate setMultiplier(IntegerExpression multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    /**
     * Gets probability.
     *
     * @return the probability
     */
    public Percentage getProbability() {
        return probability;
    }

    /**
     * Sets probability.
     *
     * @param probability the probability
     * @return the probability
     */
    public FlowRate setProbability(Percentage probability) {
        this.probability = probability;
        return this;
    }

    /**
     * Gets the current flow rate.
     *
     * @return the flow rate value
     */
    public int get() {
        int result = 0;
        float prob = evaluateProbability();
        int mult = evaluateMultiplier();

        for (int i = 0; i < mult; i++) {
            boolean take = RandomUtils.nextFloat(0.0f, 1.0f) <= prob;
            if (times % currentInterval == 0) {
                if (take)
                    result += evaluateFlowValue();

                if (interval != null && interval.isRandom())
                    currentInterval = evaluateInterval();
                else
                    currentInterval = 1;

                times = 1;
            }
            else
                times++;
        }

        return result;
    }

    /**
     *
     * @return
     */
    private float evaluateProbability() {
        return (probability != null) ? probability.getValue() / 100.0f : 1.0f;
    }

    /**
     *
     * @return
     */
    private int evaluateMultiplier() {
        return (multiplier != null) ? multiplier.eval() : 1;
    }

    /**
     *
     * @return
     */
    private int evaluateInterval() {
        return (interval != null) ? interval.eval() : 1;
    }

    /**
     *
     * @return
     */
    private int evaluateFlowValue() {
        return (value != null) ? value.eval() : 1;
    }

    /**
     * Gets the flow rate with respect to "all" expression.
     *
     * @return the "all" flow rate.
     */
    public static FlowRate all() {
        return ALL;
    }
}
