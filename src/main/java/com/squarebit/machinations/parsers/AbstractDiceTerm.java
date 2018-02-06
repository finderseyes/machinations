package com.squarebit.machinations.parsers;

import lombok.Data;

@Data
public abstract class AbstractDiceTerm {
    private int sign = 1;

    public int getSign() {
        return sign;
    }

    public AbstractDiceTerm setSign(int sign) {
        this.sign = sign;
        return this;
    }

    public abstract int evaluate();
}
