package com.squarebit.machinations.engine;

public abstract class BinaryBooleanOperator extends BooleanExpression {
    protected BooleanExpression lhs, rhs;

    public BooleanExpression getLhs() {
        return lhs;
    }

    public BinaryBooleanOperator setLhs(BooleanExpression lhs) {
        this.lhs = lhs;
        return this;
    }

    public BooleanExpression getRhs() {
        return rhs;
    }

    public BinaryBooleanOperator setRhs(BooleanExpression rhs) {
        this.rhs = rhs;
        return this;
    }
}
