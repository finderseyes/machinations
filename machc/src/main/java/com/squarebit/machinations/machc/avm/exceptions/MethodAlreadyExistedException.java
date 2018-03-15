package com.squarebit.machinations.machc.avm.exceptions;

import com.squarebit.machinations.machc.avm.TypeInfo;

public class MethodAlreadyExistedException extends Exception {
    private TypeInfo declaringType;
    private String methodName;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public MethodAlreadyExistedException(TypeInfo declaringType, String methodName) {
        super(String.format("Method with name %s already existed.", methodName));
        this.declaringType = declaringType;
        this.methodName = methodName;
    }
}
