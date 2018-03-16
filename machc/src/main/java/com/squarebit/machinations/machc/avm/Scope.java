package com.squarebit.machinations.machc.avm;

/**
 * A local-variable container.
 */
public interface Scope {
    /**
     * Gets parent scope.
     * @return parent scope, or null.
     */
    Scope getParentScope();

    /**
     * Finds a variable with given name in the scope and its parents.
     *
     * @param name the variable name
     * @return a {@link VariableInfo} instance, or null if not found.
     */
    VariableInfo findVariable(String name);

    /**
     * Gets the number of variables declared from the root scope to current scope.
     * @return the number of variable declared
     */
    int getVariableCount();

    /**
     * Gets the number of variables declared in this scope only.
     * @return the number of variables declared in this scope.
     */
    int getLocalVariableCount();
}
