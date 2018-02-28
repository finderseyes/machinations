package com.squarebit.machinations.engine;

import java.util.function.Supplier;

public class Variable extends IntegerExpression {
    private Supplier<Float> evaluator;
    private String name;

    /**
     * Determines if the expression evaluates to a random value.
     *
     * @return true if the expression value is random, false otherwise
     */
    @Override
    public boolean isRandom() {
        return false;
    }

    /**
     * Evaluates the expression to universal numerical type (float).
     *
     * @return value as float
     */
    @Override
    public float evalAsFloat() {
        return evaluator.get();
    }

    /**
     * Of variable.
     *
     * @param name      the name
     * @param evaluator the evaluator
     * @return the variable
     */
    public static Variable of(String name, Supplier<Float> evaluator) {
        Variable variable = new Variable();

        variable.name = name;
        variable.evaluator = evaluator;

        return variable;
    }
}
