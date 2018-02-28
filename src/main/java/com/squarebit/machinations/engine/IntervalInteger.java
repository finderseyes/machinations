package com.squarebit.machinations.engine;

public class IntervalInteger extends IntegerExpression {
    private IntegerExpression value;
    private IntegerExpression interval;
    private boolean random;

    private int currentInterval;
    private int times = 1;

    /**
     * Determines if the expression evaluates to a random value.
     *
     * @return true if the expression value is random, false otherwise
     */
    @Override
    public boolean isRandom() {
        return this.random;
    }

    /**
     * Evaluates the flow rate.
     *
     * @return integer value of the flow rate.
     */
    @Override
    public int eval() {
        int result = 0;

        if (times % currentInterval == 0) {
            result = value.eval();
            if (interval.isRandom())
                currentInterval = interval.eval();
            times = 1;
        }
        else
            times++;

        return result;
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
     * Creates a new instance of interval integer.
     *
     * @param value    the value
     * @param interval the interval
     * @return the interval integer instance
     */
    public static IntervalInteger of(IntegerExpression value, IntegerExpression interval) {
        IntervalInteger intervalInteger = new IntervalInteger();

        intervalInteger.value = value;
        intervalInteger.interval = interval;
        intervalInteger.random = value.isRandom() || interval.isRandom() || (interval.eval() > 1);
        intervalInteger.currentInterval = interval.eval();

        return intervalInteger;
    }
}
