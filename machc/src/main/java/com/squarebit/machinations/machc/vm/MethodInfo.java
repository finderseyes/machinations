package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.ast.GMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Information about a method during compilation.
 */
public final class MethodInfo extends SymbolInfo implements Scope {
    private TypeInfo type;

    private boolean staticMethod = false;
    private boolean returnValue = true;

    private Map<String, ArgumentInfo> argumentByName = new HashMap<>();
    private Block code;
    private List<VariableInfo> variables;

    //
    private VariableInfo thisVariable;

    /**
     * Instantiates a new Method info.
     *
     * @param type the type
     */
    public MethodInfo(TypeInfo type) {
        this(type, false, true);
    }

    /**
     * Instantiates a new Method info.
     *
     * @param type         the type
     * @param staticMethod is static method
     */
    public MethodInfo(TypeInfo type, boolean staticMethod) {
        this(type, staticMethod, true);
    }

    /**
     * Instantiates a new Method info.
     *
     * @param type           the type
     * @param staticMethod   the static method
     * @param returnValue the has return value
     */
    public MethodInfo(TypeInfo type, boolean staticMethod, boolean returnValue) {
        this.staticMethod = staticMethod;
        this.returnValue = returnValue;
        this.type = type;
        this.variables = new ArrayList<>();
        this.code = new Block(this);

        if (!staticMethod) {
            thisVariable = declareVariable();
        }
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public TypeInfo getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     * @return the type
     */
    public MethodInfo setType(TypeInfo type) {
        this.type = type;
        return this;
    }

    /**
     * Is static method boolean.
     *
     * @return the boolean
     */
    public boolean isStaticMethod() {
        return staticMethod;
    }

    /**
     * Sets static method.
     *
     * @param staticMethod the static method
     * @return the static method
     */
    public MethodInfo setStaticMethod(boolean staticMethod) {
        this.staticMethod = staticMethod;
        return this;
    }

    /**
     * Is has return value boolean.
     *
     * @return the boolean
     */
    public boolean doesReturnValue() {
        return returnValue;
    }

    /**
     * Sets has return value.
     *
     * @param returnValue the has return value
     * @return the has return value
     */
    public MethodInfo doesReturnValue(boolean returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GMethod getDeclaration() {
        return (GMethod)super.getDeclaration();
    }

    /**
     * Sets declaration.
     *
     * @param declaration the declaration
     * @return the declaration
     */
    public MethodInfo setDeclaration(GMethod declaration) {
        super.setDeclaration(declaration);
        return this;
    }

    /**
     * Declare argument argument info.
     *
     * @param name the name
     * @return the argument info
     */
    public ArgumentInfo declareArgument(String name) {
        ArgumentInfo argument = new ArgumentInfo();
        argument.setName(name);

        VariableInfo argVar = declareVariable();
        argVar.setName(String.format("__arg_%s__", name));

        argument.setVariable(argVar);

        argumentByName.put(name, argument);

        return argument;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Block getCode() {
        return code;
    }

    /**
     * Declare a local variable in this function.
     *
     * @return the variable info
     */
    public VariableInfo declareVariable() {
        VariableInfo variable = new VariableInfo();
        variable.setIndex(variables.size());
        variables.add(variable);
        return variable;
    }

    /**
     * Gets variable for "this" reference.
     *
     * @return the this variable
     */
    public VariableInfo getThisVariable() {
        return thisVariable;
    }

    /**
     * Gets variables of this method.
     *
     * @return the variables
     */
    public List<VariableInfo> getVariables() {
        return variables;
    }

    /**
     * Gets the number of variables declared in this method, including argument variables.
     *
     * @return the number of variables.
     */
    public int variableCount() {
        return this.variables.size();
    }

    /**
     * Gets the number of variables used by method arguments, including "this" argument.
     *
     * @return the number of argument variable count
     */
    public int argumentVariableCount() {
        if (staticMethod)
            return this.argumentByName.size();
        else
            return this.argumentByName.size() + 1;
    }

    /**
     * Gets the parent scope.
     *
     * @return parent scope.
     */
    @Override
    public Scope getParent() {
        return type;
    }

    /**
     * Finds a local symbol with given name in this scope.
     *
     * @param name the symbol name
     * @return the symbol or null.
     */
    @Override
    public SymbolInfo findLocalSymbol(String name) {
        return null;
    }
}
