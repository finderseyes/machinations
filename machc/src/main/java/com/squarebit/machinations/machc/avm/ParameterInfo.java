package com.squarebit.machinations.machc.avm;

/**
 * A parameter declared in a {@link MethodInfo}.
 */
public final class ParameterInfo {
    private MethodInfo declaringMethod;
    private String name;

    /**
     * Instantiates a new instance.
     */
    public ParameterInfo() {
    }

    /**
     * Gets declaring method.
     *
     * @return the declaring method
     */
    public MethodInfo getDeclaringMethod() {
        return declaringMethod;
    }

    /**
     * Sets declaring method.
     *
     * @param declaringMethod the declaring method
     * @return this instance
     */
    public ParameterInfo setDeclaringMethod(MethodInfo declaringMethod) {
        this.declaringMethod = declaringMethod;
        return this;
    }

    /**
     * Gets parameter name.
     *
     * @return the parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets parameter name.
     *
     * @param name the parameter name
     * @return this instance
     */
    public ParameterInfo setName(String name) {
        this.name = name;
        return this;
    }
}
