package com.squarebit.machinations.machc.ast.expressions;

public class GSetElementDescriptor {
    private final GExpression size;
    private final GInteger capacity;
    private final GString name;

    public GSetElementDescriptor(GExpression size, GInteger capacity, GString name) {
        this.size = size;
        this.capacity = capacity;
        this.name = name;
    }

    public GExpression getSize() {
        return size;
    }

    public GInteger getCapacity() {
        return capacity;
    }

    public GString getName() {
        return name;
    }
}
