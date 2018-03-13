package com.squarebit.machinations.machc.vm.expressions;

import com.squarebit.machinations.machc.vm.TObject;

/**
 * The expression evaluation context.
 */
public final class EvaluationContext {
    private final TObject[] variables;

    /**
     * Instantiates a new Evaluation context.
     *
     * @param variables the variables
     */
    public EvaluationContext(TObject[] variables) {
        this.variables = variables;
    }

    /**
     * Gets variable.
     *
     * @param index the index
     * @return the variable
     */
    public TObject getVariable(int index) {
        return variables[index];
    }
}
