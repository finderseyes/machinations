package com.squarebit.machinations.machc.avm.expressions;

/**
 * Comparison.
 */
public class Compare extends Expression {
    /**
     * The enum Operator.
     */
    public enum Operator {
        GT,
        GTE,
        LT,
        LTE,
        EQ,
        NEQ;

        /**
         * Parse operator.
         *
         * @param value the value
         * @return the operator
         */
        public static Operator parse(String value) {
            if (value.equals(">"))
                return GT;
            else if (value.equals(">="))
                return GTE;
            else if (value.equals("<"))
                return LT;
            else if (value.equals("<="))
                return LTE;
            else if (value.equals("=="))
                return EQ;
            else if (value.equals("!="))
                return NEQ;
            else
                throw new RuntimeException("Unknown operator.");
        }
    }

    private final Operator operator;
    private final Expression first, second;

    /**
     * Instantiates a new Compare.
     *
     * @param operator the operator
     * @param first    the first
     * @param second   the second
     */
    public Compare(Operator operator, Expression first, Expression second) {
        this.operator = operator;
        this.first = first;
        this.second = second;
    }

    /**
     * Gets operator.
     *
     * @return the operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Gets first.
     *
     * @return the first
     */
    public Expression getFirst() {
        return first;
    }

    /**
     * Gets second.
     *
     * @return the second
     */
    public Expression getSecond() {
        return second;
    }
}
