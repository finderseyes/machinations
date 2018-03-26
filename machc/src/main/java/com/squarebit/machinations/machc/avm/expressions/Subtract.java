package com.squarebit.machinations.machc.avm.expressions;

public class Subtract extends Expression {
    private final Expression first, second;

    /**
     * Instantiates a new Subtract.
     *
     * @param first  the first
     * @param second the second
     */
    public Subtract(Expression first, Expression second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets first.
     *
     * @return the first
     */
    public Expression getFirst() {
        return first;
    }

    /**
     * Gets second.
     *
     * @return the second
     */
    public Expression getSecond() {
        return second;
    }
}
