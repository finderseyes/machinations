package com.squarebit.machinations.machc.avm.exceptions;

public class UnknownIdentifierException extends CompilationException {
    private final String identifier;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public UnknownIdentifierException(String identifier) {
        super(String.format("Unknown identifier %s", identifier));
        this.identifier = identifier;
    }
}
