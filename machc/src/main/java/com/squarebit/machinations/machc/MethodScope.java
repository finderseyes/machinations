package com.squarebit.machinations.machc;

/**
 * The scope when calling a method-based object.
 */
public class MethodScope extends Scope {
    private MethodBase method;

    /**
     * Instantiates a new method scope.
     *
     * @param parent the parent
     * @param method the method
     */
    public MethodScope(Scope parent, MethodBase method) {
        super(parent);
        this.method = method;
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public MethodBase getMethod() {
        return method;
    }

    /**
     * Finds a symbol with given name within this scope.
     *
     * @param name the synmbol name
     * @return the symbol or null.
     */
    @Override
    protected Object findLocalSymbol(String name) {
        return null;
    }
}
