package com.squarebit.machinations.engine;

/**
 * An expression that support evaluation of a flow rate.
 */
public abstract class IntegerExpression extends ArithmeticExpression {
    /**
     * Determines if the expression evaluates to a random value.
     *
     * @return true if the expression value is random, false otherwise
     */
    public abstract boolean isRandom();

    /**
     * Evaluates the flow rate.
     *
     * @return integer value of the flow rate.
     */
    public int eval() {
        return (int)Math.floor(this.evalAsFloat());
    }
}
