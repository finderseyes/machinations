package com.squarebit.machinations.engine;

public abstract class BinaryOperator extends ArithmeticExpression {
    protected IntegerExpression lhs, rhs;

    /**
     * Instantiates a new AdditionIntegerExpression.
     */
    public BinaryOperator() {

    }

    /**
     * Gets lhs.
     *
     * @return the lhs
     */
    public IntegerExpression getLhs() {
        return lhs;
    }

    /**
     * Sets lhs.
     *
     * @param lhs the lhs
     * @return the lhs
     */
    public BinaryOperator setLhs(IntegerExpression lhs) {
        this.lhs = lhs;
        return this;
    }

    /**
     * Gets rhs.
     *
     * @return the rhs
     */
    public IntegerExpression getRhs() {
        return rhs;
    }

    /**
     * Sets rhs.
     *
     * @param rhs the rhs
     * @return the rhs
     */
    public BinaryOperator setRhs(IntegerExpression rhs) {
        this.rhs = rhs;
        return this;
    }
}
