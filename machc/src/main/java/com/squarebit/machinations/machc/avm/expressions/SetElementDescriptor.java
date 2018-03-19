package com.squarebit.machinations.machc.avm.expressions;

public class SetElementDescriptor {
    private Expression size;
    private Constant capacity;
    private Constant name;

    public SetElementDescriptor(Expression size, Constant capacity, Constant name) {
        this.size = size;
        this.capacity = capacity;
        this.name = name;
    }

    public Expression getSize() {
        return size;
    }

    public Constant getCapacity() {
        return capacity;
    }

    public Constant getName() {
        return name;
    }
}
