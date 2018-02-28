package com.squarebit.machinations.engine;

public abstract class BinaryLogicalOperator extends LogicalExpression {
    protected LogicalExpression lhs, rhs;

    public LogicalExpression getLhs() {
        return lhs;
    }

    public BinaryLogicalOperator setLhs(LogicalExpression lhs) {
        this.lhs = lhs;
        return this;
    }

    public LogicalExpression getRhs() {
        return rhs;
    }

    public BinaryLogicalOperator setRhs(LogicalExpression rhs) {
        this.rhs = rhs;
        return this;
    }
}
