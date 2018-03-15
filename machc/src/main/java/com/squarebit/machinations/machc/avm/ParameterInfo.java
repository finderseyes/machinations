package com.squarebit.machinations.machc.avm;

/**
 * A parameter declared in a {@link MethodInfo}.
 */
public final class ParameterInfo {
    private MethodInfo declaringMethod;
    private VariableInfo variable;

    /**
     * Instantiates a new instance.
     */
    public ParameterInfo() {
        this.variable = new VariableInfo();
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
        return variable.getName();
    }

    /**
     * Sets parameter name.
     *
     * @param name the parameter name
     * @return this instance
     */
    public ParameterInfo setName(String name) {
        this.variable.setName(name);
        return this;
    }

    /**
     * Gets parameter type.
     *
     * @return the parameter type.
     */
    public TypeInfo getType() {
        return variable.getType();
    }

    /**
     * Sets parameter type.
     *
     * @param type the type
     * @return this instance
     */
    public ParameterInfo setType(TypeInfo type) {
        this.variable.setType(type);
        return this;
    }

    /**
     * Gets the variable associated with this parameter.
     *
     * @return the variable
     */
    public VariableInfo getVariable() {
        return variable;
    }
}
