package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.ast.GMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Information about a method during compilation.
 */
public final class MethodInfo extends SymbolInfo implements Scope {
    private TypeInfo type;

    private boolean staticMethod = false;

    private Map<String, ArgumentInfo> argumentByName;
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
        this(type, false);
    }

    /**
     * Instantiates a new Method info.
     *
     * @param type         the type
     * @param staticMethod is static method
     */
    public MethodInfo(TypeInfo type, boolean staticMethod) {
        this.staticMethod = staticMethod;
        this.type = type;
        this.variables = new ArrayList<>();
        this.code = new Block(this);

        if (!staticMethod) {
            thisVariable = declareVariable();
        }
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
    public SymbolInfo findSymbol(String name) {
        return null;
    }
}
