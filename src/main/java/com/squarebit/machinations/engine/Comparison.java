package com.squarebit.machinations.engine;

public class Comparison extends RelationOperator {
    public static final String GT = ">";
    public static final String GTE = ">=";
    public static final String LT = "<";
    public static final String LTE = "<=";
    public static final String EQ = "==";
    public static final String NEQ = "!=";

    private String operator;

    /**
     * Gets operator.
     *
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets operator.
     *
     * @param operator the operator
     * @return the operator
     */
    public Comparison setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Evaluate boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean evaluate() {
        if (this.operator != null) {
            if (this.operator.equals(GT))
                return this.lhs.evaluate() > this.rhs.evaluate();
            else if (this.operator.equals(GTE))
                return this.lhs.evaluate() >= this.rhs.evaluate();
            else if (this.operator.equals(LT))
                return this.lhs.evaluate() < this.rhs.evaluate();
            else if (this.operator.equals(LTE))
                return this.lhs.evaluate() <= this.rhs.evaluate();
            else if (this.operator.equals(EQ))
                return this.lhs.evaluate() == this.rhs.evaluate();
            else if (this.operator.equals(NEQ))
                return this.lhs.evaluate() != this.rhs.evaluate();
        }
        return false;
    }

    public static Comparison of(String op, ArithmeticExpression lhs, ArithmeticExpression rhs) {
        return (Comparison)(new Comparison().setOperator(op).setLhs(lhs).setRhs(rhs));
    }
}
