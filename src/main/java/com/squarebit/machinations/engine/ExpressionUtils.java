package com.squarebit.machinations.engine;

public class ExpressionUtils {
    /**
     * Determines if an arithmetic expression contains probable numbers.
     *
     * @param expression the expression
     * @return the boolean
     */
    public static boolean isProbable(Expression expression) {
        if (expression instanceof ProbableNumber)
            return true;
        else if (expression instanceof BinaryOperator) {
            BinaryOperator binary = (BinaryOperator)expression;
            return isProbable(binary.lhs) || isProbable(binary.rhs);
        }

        return false;
    }
}
