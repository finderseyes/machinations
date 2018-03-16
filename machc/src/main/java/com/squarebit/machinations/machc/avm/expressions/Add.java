package com.squarebit.machinations.machc.avm.expressions;

public final class Add extends Expression {
    public final Expression first, second;

    /**
     * Instantiates a new Add.
     *
     * @param first  the first
     * @param second the second
     */
    public Add(Expression first, Expression second) {
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
