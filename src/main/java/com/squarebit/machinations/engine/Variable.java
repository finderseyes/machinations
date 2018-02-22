package com.squarebit.machinations.engine;

import java.util.function.Supplier;

public class Variable extends ArithmeticExpression {
    private Supplier<Integer> evaluator;
    private String name;

    /**
     * Gets evaluator.
     *
     * @return the evaluator
     */
    public Supplier<Integer> getEvaluator() {
        return evaluator;
    }

    /**
     * Sets evaluator.
     *
     * @param evaluator the evaluator
     * @return the evaluator
     */
    public Variable setEvaluator(Supplier<Integer> evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     */
    public Variable setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int evaluate() {
        return evaluator.get();
    }

    /**
     * Evaluate as probable and return probability.
     *
     * @return probability
     */
    @Override
    public float nonZeroProbability() {
        if (evaluator.get() >= 1)
            return 1.0f;
        else
            return 0.0f;
    }

    /**
     * Of variable.
     *
     * @param name      the name
     * @param evaluator the evaluator
     * @return the variable
     */
    public static Variable of(String name, Supplier<Integer> evaluator) {
        return (Variable)(new Variable().setName(name).setEvaluator(evaluator));
    }
}
