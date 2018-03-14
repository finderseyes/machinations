package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.FixedInteger;
import com.squarebit.machinations.engine.IntegerExpression;

/**
 * The register.
 */
public class Register extends Node {
    private IntegerExpression value = FixedInteger.of(0);

    /**
     * Gets value.
     *
     * @return the value
     */
    public IntegerExpression getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     * @return the value
     */
    public Register setValue(IntegerExpression value) {
        this.value = value;
        return this;
    }

    /**
     * Evaluate int.
     *
     * @param context the context
     * @return the int
     */
    @Override
    public int evaluate(NodeEvaluationContext context) {
        return this.value.eval();
    }
}
