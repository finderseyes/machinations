package com.squarebit.machinations.machc.ast.expressions;

/**
 * Expression involves 2 sub-expressions (first op second).
 */
public class GBinaryExpression implements GExpression {
    private GExpression first, second;
    private String operator;

    /**
     * Gets the first expression.
     *
     * @return the first expression
     */
    public GExpression getFirst() {
        return first;
    }

    /**
     * Sets the first expression.
     *
     * @param first the first expression
     * @return the first expression
     */
    public GBinaryExpression setFirst(GExpression first) {
        this.first = first;
        return this;
    }

    /**
     * Gets the second expression.
     *
     * @return the second expression
     */
    public GExpression getSecond() {
        return second;
    }

    /**
     * Sets the second expression.
     *
     * @param second the second expression
     * @return the second expression
     */
    public GBinaryExpression setSecond(GExpression second) {
        this.second = second;
        return this;
    }

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
    public GBinaryExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }
}
