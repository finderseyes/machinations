package com.squarebit.machinations.engine;

public abstract class RelationOperator extends BooleanExpression {
    protected ArithmeticExpression lhs, rhs;

    public ArithmeticExpression getLhs() {
        return lhs;
    }

    public RelationOperator setLhs(ArithmeticExpression lhs) {
        this.lhs = lhs;
        return this;
    }

    public ArithmeticExpression getRhs() {
        return rhs;
    }

    public RelationOperator setRhs(ArithmeticExpression rhs) {
        this.rhs = rhs;
        return this;
    }
}
