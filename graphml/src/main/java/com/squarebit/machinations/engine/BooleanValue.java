package com.squarebit.machinations.engine;

public class BooleanValue extends LogicalExpression {
    private boolean value = false;

    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean eval() {
        return value;
    }

    /**
     * Creates new boolean value instance.
     *
     * @param value the value
     * @return the boolean value
     */
    public static BooleanValue of(boolean value) {
        BooleanValue booleanValue = new BooleanValue();
        booleanValue.value = value;
        return booleanValue;
    }
}
