package com.squarebit.machinations.machc.avm.exceptions;

import com.squarebit.machinations.machc.avm.ModuleInfo;

public class TypeAlreadyExistedException extends Exception {
    private ModuleInfo moduleInfo;
    private String typeName;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public TypeAlreadyExistedException(ModuleInfo moduleInfo, String typeName) {
        super(String.format("Module already has a type with given name %s", typeName));
        this.moduleInfo = moduleInfo;
        this.typeName = typeName;
    }
}
