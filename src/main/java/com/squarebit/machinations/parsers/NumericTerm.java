package com.squarebit.machinations.parsers;

import lombok.Data;

@Data
public class NumericTerm extends AbstractDiceTerm {
    private int value = 1;

    public int getValue() {
        return value;
    }

    public NumericTerm setValue(int value) {
        this.value = value;
        return this;
    }

    @Override
    public int evaluate() {
        return this.value * this.getSign();
    }
}
