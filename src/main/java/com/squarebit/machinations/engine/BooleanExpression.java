package com.squarebit.machinations.engine;

public abstract class BooleanExpression extends Expression {
    @Override
    public DataType getType() {
        return DataType.BOOLEAN;
    }

    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    public abstract boolean evaluate();
}
