package com.squarebit.machinations.machc.avm.expressions;

public class SetElementDescriptor {
    private Variable size;
    private Constant capacity;
    private Constant name;

    public SetElementDescriptor(Variable size, Constant capacity, Constant name) {
        this.size = size;
        this.capacity = capacity;
        this.name = name;
    }

    public Variable getSize() {
        return size;
    }

    public Constant getCapacity() {
        return capacity;
    }

    public Constant getName() {
        return name;
    }
}
