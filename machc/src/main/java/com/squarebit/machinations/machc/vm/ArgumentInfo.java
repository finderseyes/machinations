package com.squarebit.machinations.machc.vm;


/**
 * Method argument.
 */
public final class ArgumentInfo extends SymbolInfo {
    private VariableInfo variable;

    /**
     * Gets variable.
     *
     * @return the variable
     */
    public VariableInfo getVariable() {
        return variable;
    }

    /**
     * Sets variable.
     *
     * @param variable the variable
     * @return the variable
     */
    public ArgumentInfo setVariable(VariableInfo variable) {
        this.variable = variable;
        return this;
    }
}
