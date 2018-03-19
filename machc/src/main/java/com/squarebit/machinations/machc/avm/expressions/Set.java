package com.squarebit.machinations.machc.avm.expressions;

public class Set extends Expression {
    private SetDescriptor descriptor;

    public Set(SetDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public SetDescriptor getDescriptor() {
        return descriptor;
    }
}
