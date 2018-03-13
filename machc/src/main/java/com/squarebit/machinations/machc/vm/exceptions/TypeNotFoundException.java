package com.squarebit.machinations.machc.vm.exceptions;

public final class TypeNotFoundException extends Exception {
    private String typeName;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public TypeNotFoundException(String typeName) {
        super(String.format("Type %s is not found", typeName));
        this.typeName = typeName;
    }
}
