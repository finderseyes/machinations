package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.BooleanValue;
import com.squarebit.machinations.engine.FixedInteger;
import com.squarebit.machinations.engine.IntegerExpression;
import com.squarebit.machinations.engine.LogicalExpression;

/**
 * A flow rate.
 */
public class FlowRate {
    private static final FlowRate ALL = new FlowRate();
    private static final IntegerExpression DEFAULT_VALUE = FixedInteger.of(1.0f);
    private static final IntegerExpression DEFAULT_INTERVAL = FixedInteger.of(1.0f);
    private static final IntegerExpression DEFAULT_MULTIPLIER = FixedInteger.of(1.0f);
    private static final LogicalExpression DEFAULT_CONDITION = BooleanValue.of(true);

    private IntegerExpression value = DEFAULT_VALUE;
    private IntegerExpression interval = DEFAULT_INTERVAL;
    private IntegerExpression multiplier = DEFAULT_MULTIPLIER;
    private float probability = 1.0f;
    private LogicalExpression condition = DEFAULT_CONDITION;


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
    public float getProbability() {
        return probability;
    }

    /**
     * Sets probability.
     *
     * @param probability the probability
     * @return the probability
     */
    public FlowRate setProbability(float probability) {
        this.probability = probability;
        return this;
    }

    /**
     * Gets condition.
     *
     * @return the condition
     */
    public LogicalExpression getCondition() {
        return condition;
    }

    /**
     * Sets condition.
     *
     * @param condition the condition
     * @return the condition
     */
    public FlowRate setCondition(LogicalExpression condition) {
        this.condition = condition;
        return this;
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
