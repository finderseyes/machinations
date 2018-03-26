package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.ast.GMethod;
import com.squarebit.machinations.machc.avm.exceptions.ParameterAlreadyExistedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A method declared in a {@link TypeInfo}.
 */
public final class MethodInfo implements Scope, Symbol {
    public static final String CONSTRUCTOR_NAME = "$__ctor__$";

    private GMethod declaration;
    private TypeInfo declaringType;
    private String name;

    private boolean isStatic;
    private boolean isConstructor;

    /////////////////////////
    // Parameters
    private List<ParameterInfo> parameters;
    private Map<String, ParameterInfo> parameterByName;

    /////////////////////////
    // Code
    private VariableInfo thisVariable = null;
    private InstructionBlock instructionBlock;

    // Signature.
    private MethodSignature signature;

    /**
     * Instantiates a new instance.
     */
    public MethodInfo() {
        this.isStatic = false;
        this.isConstructor = true;

        this.parameters = new ArrayList<>();
        this.parameterByName = new HashMap<>();

        this.instructionBlock = new InstructionBlock().setParentScope(this);
        this.thisVariable = new VariableInfo()
                .setDeclaringScope(this)
                .setIndex(0)
                .setName("$this");

        this.signature = new MethodSignature();
    }

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GMethod getDeclaration() {
        return declaration;
    }

    /**
     * Sets declaration.
     *
     * @param declaration the declaration
     * @return the declaration
     */
    public MethodInfo setDeclaration(GMethod declaration) {
        this.declaration = declaration;
        return this;
    }

    /**
     * Sets the method static status.
     *
     * @param isStatic static status
     * @return this instance.
     */
    public MethodInfo setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    /**
     * Determines if the method is static.
     *
     * @return true or false
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * Is constructor boolean.
     *
     * @return the boolean
     */
    public boolean isConstructor() {
        return isConstructor;
    }

    /**
     * Sets constructor.
     *
     * @param constructor the constructor
     * @return the constructor
     */
    public MethodInfo setConstructor(boolean constructor) {
        isConstructor = constructor;
        return this;
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
        this.thisVariable.setType(declaringType);
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
        this.signature.setName(name);
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
     * Gets parameter count.
     *
     * @return the parameter count
     */
    public int getParameterCount() {
        return this.parameters.size();
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
        variableInfo.setDeclaringScope(this).setIndex(getVariableCount());

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

    /**
     * Gets the number of variables declared from the root scope to current scope.
     *
     * @return the number of variable declared
     */
    @Override
    public int getVariableCount() {
        return isStatic ? this.parameters.size() : this.parameters.size() + 1;
    }

    /**
     * Gets the number of variables declared in this scope only.
     *
     * @return the number of variables declared in this scope.
     */
    @Override
    public int getLocalVariableCount() {
        return this.getVariableCount();
    }

    /**
     * Gets this variable.
     *
     * @return the this variable
     */
    public VariableInfo getThisVariable() {
        return thisVariable;
    }

    /**
     * Gets instruction block.
     *
     * @return the instruction block
     */
    public InstructionBlock getInstructionBlock() {
        return instructionBlock;
    }

    /**
     * Sets instruction block.
     *
     * @param instructionBlock the instruction block
     * @return the instruction block
     */
    public MethodInfo setInstructionBlock(InstructionBlock instructionBlock) {
        this.instructionBlock = instructionBlock;
        return this;
    }
}
