package com.squarebit.machinations.machc.avm;

/**
 * A method declared in a {@link TypeInfo}.
 */
public final class MethodInfo {
    private TypeInfo declaringType;
    private String name;

    /**
     * Instantiates a new instance.
     */
    public MethodInfo() {
    }

    /**
     * Gets declaring {@link TypeInfo}.
     *
     * @return the declaring {@link TypeInfo}
     */
    public TypeInfo getDeclaringType() {
        return declaringType;
    }

    /**
     * Sets declaring {@link TypeInfo}.
     *
     * @param declaringType the declaring {@link TypeInfo}
     * @return this instance
     * @apiNote it is not recommended to use this method directly, use {@link TypeInfo#createMethod(String)} or
     * {@link TypeInfo#addMethod(MethodInfo)} instead.
     */
    public MethodInfo setDeclaringType(TypeInfo declaringType) {
        this.declaringType = declaringType;
        return this;
    }

    /**
     * Gets the method name.
     *
     * @return the method name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the method name.
     *
     * @param name the method name
     * @return this instance
     * @apiNote it is not recommended to use this method directly, use {@link TypeInfo#createMethod(String)} or
     * {@link TypeInfo#addMethod(MethodInfo)} instead.
     */
    public MethodInfo setName(String name) {
        this.name = name;
        return this;
    }
}
