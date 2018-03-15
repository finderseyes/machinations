package com.squarebit.machinations.machc.avm.expressions;

import com.squarebit.machinations.machc.avm.runtime.TObject;

public class Constant extends Expression {
    private final TObject value;

    public Constant(TObject value) {
        this.value = value;
    }

    public TObject getValue() {
        return value;
    }
}
