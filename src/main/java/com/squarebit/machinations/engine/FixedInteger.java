package com.squarebit.machinations.engine;

/**
 * A fixed flow rate expression.
 */
public class FixedInteger extends IntegerExpression {
    protected float value = 1.0f;

    /**
     * Determines if the expression evaluates to a random value.
     *
     * @return true if the expression value is random, false otherwise
     */
    @Override
    public boolean isRandom() {
        return false;
    }

    /**
     * Evaluates the expression to universal numerical type (float).
     *
     * @return value as float
     */
    @Override
    public float evalAsFloat() {
        return value;
    }

    /**
     * Creates a fixed flow rate instance.
     *
     * @param value the flow value
     * @return the fixed flow rate instance
     */
    public static FixedInteger of(float value) {
        FixedInteger fixedFlowRate = new FixedInteger();
        fixedFlowRate.value = value;
        return fixedFlowRate;
    }

    /**
     * Parse fixed integer.
     *
     * @param text the text
     * @return the fixed integer
     */
    public static FixedInteger parse(String text) {
        return FixedInteger.of(Float.parseFloat(text));
    }
}
