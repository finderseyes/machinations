package com.squarebit.machinations.machc.ast.expressions;

public class GAssignment implements GExpression {
    private GAssignmentTarget target;
    private GExpression expression;

    /**
     * Gets target.
     *
     * @return the target
     */
    public GAssignmentTarget getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target the target
     * @return the target
     */
    public GAssignment setTarget(GAssignmentTarget target) {
        this.target = target;
        return this;
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public GExpression getExpression() {
        return expression;
    }

    /**
     * Sets expression.
     *
     * @param expression the expression
     * @return the expression
     */
    public GAssignment setExpression(GExpression expression) {
        this.expression = expression;
        return this;
    }
}
