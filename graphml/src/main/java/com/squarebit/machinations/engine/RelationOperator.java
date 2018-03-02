package com.squarebit.machinations.engine;

public abstract class RelationOperator extends LogicalExpression {
    protected IntegerExpression lhs, rhs;

    public IntegerExpression getLhs() {
        return lhs;
    }

    public RelationOperator setLhs(IntegerExpression lhs) {
        this.lhs = lhs;
        return this;
    }

    public IntegerExpression getRhs() {
        return rhs;
    }

    public RelationOperator setRhs(IntegerExpression rhs) {
        this.rhs = rhs;
        return this;
    }
}
