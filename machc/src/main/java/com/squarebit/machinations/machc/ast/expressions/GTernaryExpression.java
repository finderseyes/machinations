package com.squarebit.machinations.machc.ast.expressions;

/**
 * The ternary expression (condition ? first : second).
 */
public final class GTernaryExpression extends GExpression {
    private GExpression condition;
    private GExpression first;
    private GExpression second;

    /**
     * Gets condition.
     *
     * @return the condition
     */
    public GExpression getCondition() {
        return condition;
    }

    /**
     * Sets condition.
     *
     * @param condition the condition
     * @return the condition
     */
    public GTernaryExpression setCondition(GExpression condition) {
        this.condition = condition;
        return this;
    }

    /**
     * Gets first.
     *
     * @return the first
     */
    public GExpression getFirst() {
        return first;
    }

    /**
     * Sets first.
     *
     * @param first the first
     * @return the first
     */
    public GTernaryExpression setFirst(GExpression first) {
        this.first = first;
        return this;
    }

    /**
     * Gets second.
     *
     * @return the second
     */
    public GExpression getSecond() {
        return second;
    }

    /**
     * Sets second.
     *
     * @param second the second
     * @return the second
     */
    public GTernaryExpression setSecond(GExpression second) {
        this.second = second;
        return this;
    }
}
