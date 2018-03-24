package com.squarebit.machinations.machc.ast.expressions;

/**
 * For post incremental/decremental.
 */
public final class GPostfixExpression implements GExpression {
    public enum Operator {
        INCREMENT,
        DECREMENT;

        public static Operator parse(String text) {
            if (text.equals("++")) return INCREMENT;
            else if (text.equals("--")) return DECREMENT;
            else throw new RuntimeException("Unknown operator.");
        }
    }

    private Operator operator;
    private GAssignmentTarget expression;

    /**
     * Gets operator.
     *
     * @return the operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Sets operator.
     *
     * @param operator the operator
     * @return the operator
     */
    public GPostfixExpression setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public GAssignmentTarget getExpression() {
        return expression;
    }

    /**
     * Sets expression.
     *
     * @param expression the expression
     * @return the expression
     */
    public GPostfixExpression setExpression(GAssignmentTarget expression) {
        this.expression = expression;
        return this;
    }
}
