package com.squarebit.machinations.machc;

public class MethodScope extends Scope {
    private MethodBase method;

    public MethodScope(Scope parent, MethodBase method) {
        super(parent);
        this.method = method;
    }

    public MethodBase getMethod() {
        return method;
    }

    @Override
    protected Object findLocalSymbol(String name) {
        return null;
    }
}
