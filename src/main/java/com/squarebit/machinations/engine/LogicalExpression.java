package com.squarebit.machinations.engine;

public abstract class LogicalExpression extends Expression {
    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    public abstract boolean eval();
}
