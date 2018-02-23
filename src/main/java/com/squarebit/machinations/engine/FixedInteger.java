package com.squarebit.machinations.engine;

/**
 * A fixed flow rate expression.
 */
public class FixedInteger extends IntegerExpression {
    private float value = 1.0f;

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
     * Evaluates the flow rate.
     *
     * @return integer value of the flow rate.
     */
    @Override
    public int eval() {
        return (int)Math.floor(value);
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
}
