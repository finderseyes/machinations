package com.squarebit.machinations.machc.ast;

public class GConnection extends GGraphField {
    private GConnectionDescriptor descriptor;

    public GConnectionDescriptor getDescriptor() {
        return descriptor;
    }

    public GConnection setDescriptor(GConnectionDescriptor descriptor) {
        this.descriptor = descriptor;
        return this;
    }
}
