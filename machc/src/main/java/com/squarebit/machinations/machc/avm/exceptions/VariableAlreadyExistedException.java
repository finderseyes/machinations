package com.squarebit.machinations.machc.avm.exceptions;

import com.squarebit.machinations.machc.avm.Scope;

public class VariableAlreadyExistedException extends Exception {
    private Scope scope;
    private String variableName;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public VariableAlreadyExistedException(Scope scope, String variableName) {
        super(String.format("Variable with name %s already existed.", variableName));
        this.scope = scope;
        this.variableName = variableName;
    }
}
