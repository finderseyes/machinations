package com.squarebit.machinations.machc.avm.exceptions;

import com.squarebit.machinations.machc.avm.TypeInfo;

public final class FieldAlreadyExistedException extends Exception {
    private TypeInfo declaringType;
    private String fieldName;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public FieldAlreadyExistedException(TypeInfo declaringType, String fieldName) {
        super(String.format("Field with name %s already existed.", fieldName));
        this.declaringType = declaringType;
        this.fieldName = fieldName;
    }
}
