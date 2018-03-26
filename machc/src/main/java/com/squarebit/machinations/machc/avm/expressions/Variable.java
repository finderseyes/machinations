package com.squarebit.machinations.machc.avm.expressions;

import com.squarebit.machinations.machc.avm.VariableInfo;

/**
 * An expression whose value is stored in a local variable. It would be the result of a complex expression or
 * intermediate result or other expressions.
 */
public class Variable extends Expression {
    private VariableInfo variableInfo;
    private VariableData data;

    /**
     * Instantiates a new Variable.
     *
     * @param variableInfo the variable info
     */
    public Variable(VariableInfo variableInfo) {
        this.variableInfo = variableInfo;
    }

    /**
     * Gets variable info.
     *
     * @return the variable info
     */
    public VariableInfo getVariableInfo() {
        return variableInfo;
    }

    /**
     * Sets variable info.
     *
     * @param variableInfo the variable info
     * @return the variable info
     */
    public Variable setVariableInfo(VariableInfo variableInfo) {
        this.variableInfo = variableInfo;
        return this;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public VariableData getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     * @return the data
     */
    public Variable setData(VariableData data) {
        this.data = data;
        return this;
    }
}
