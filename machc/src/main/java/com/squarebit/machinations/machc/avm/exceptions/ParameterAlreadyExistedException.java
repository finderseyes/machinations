package com.squarebit.machinations.machc.avm.exceptions;

import com.squarebit.machinations.machc.avm.MethodInfo;

public final class ParameterAlreadyExistedException extends Exception {
    private MethodInfo method;
    private String parameterName;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ParameterAlreadyExistedException(MethodInfo method, String parameterName) {
        super(String.format("Parameter with name %s already existed.", parameterName));
        this.method = method;
        this.parameterName = parameterName;
    }
}
