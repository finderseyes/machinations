package com.squarebit.machinations.engine;

public abstract class BinaryOperator extends ArithmeticExpression {
    protected ArithmeticExpression lhs, rhs;

    /**
     * Instantiates a new Addition.
     */
    public BinaryOperator() {

    }

    /**
     * Instantiates a new Addition.
     *
     * @param lhs the lhs
     * @param rhs the rhs
     */
    public BinaryOperator(ArithmeticExpression lhs, ArithmeticExpression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * Gets lhs.
     *
     * @return the lhs
     */
    public ArithmeticExpression getLhs() {
        return lhs;
    }

    /**
     * Sets lhs.
     *
     * @param lhs the lhs
     * @return the lhs
     */
    public BinaryOperator setLhs(ArithmeticExpression lhs) {
        this.lhs = lhs;
        return this;
    }

    /**
     * Gets rhs.
     *
     * @return the rhs
     */
    public ArithmeticExpression getRhs() {
        return rhs;
    }

    /**
     * Sets rhs.
     *
     * @param rhs the rhs
     * @return the rhs
     */
    public BinaryOperator setRhs(ArithmeticExpression rhs) {
        this.rhs = rhs;
        return this;
    }
}
