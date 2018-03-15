package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.exceptions.ParameterAlreadyExistedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A method declared in a {@link TypeInfo}.
 */
public final class MethodInfo implements Scope {
    private TypeInfo declaringType;
    private String name;

    /////////////////////////
    // Parameters
    private List<ParameterInfo> parameters;
    private Map<String, ParameterInfo> parameterByName;

    /////////////////////////
    // Code
    private InstructionBlock instructionBlock;

    /**
     * Instantiates a new instance.
     */
    public MethodInfo() {
        this.parameters = new ArrayList<>();
        this.parameterByName = new HashMap<>();

        this.instructionBlock = new InstructionBlock().setParentScope(this);
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

    /**
     * Gets the parameter list.
     *
     * @return the parameter list, in declaration order
     */
    public List<ParameterInfo> getParameters() {
        return parameters;
    }

    /**
     * Creates a {@link ParameterInfo} and add it to this method parameters.
     *
     * @param name the parameter name
     * @return the {@link ParameterInfo} instance
     * @throws ParameterAlreadyExistedException if a parameter with given name already existed.
     */
    public ParameterInfo createParameter(String name) throws ParameterAlreadyExistedException {
        ParameterInfo parameterInfo = new ParameterInfo().setName(name);
        addParameter(parameterInfo);
        return parameterInfo;
    }

    /**
     * Adds a {@link ParameterInfo} to this method parameters.
     *
     * @param parameterInfo the parameter
     * @throws ParameterAlreadyExistedException if a parameter with given name already existed.
     */
    public void addParameter(ParameterInfo parameterInfo) throws ParameterAlreadyExistedException {
        if (parameterByName.containsKey(parameterInfo.getName()))
            throw new ParameterAlreadyExistedException(this, parameterInfo.getName());

        parameterInfo.setDeclaringMethod(this);

        VariableInfo variableInfo = parameterInfo.getVariable();
        variableInfo.setDeclaringScope(this).setIndex(parameters.size());

        parameters.add(parameterInfo);
        parameterByName.put(parameterInfo.getName(), parameterInfo);
    }

    /**
     * Gets parent scope.
     *
     * @return parent scope, or null.
     */
    @Override
    public Scope getParentScope() {
        return null;
    }

    /**
     * Finds a variable with given name in the scope.
     *
     * @param name the variable name
     * @return a {@link VariableInfo} instance, or null if not found.
     */
    @Override
    public VariableInfo findVariable(String name) {
        ParameterInfo parameterInfo = parameterByName.getOrDefault(name, null);
        return (parameterInfo == null) ? null : parameterInfo.getVariable();
    }
}
