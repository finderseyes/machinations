package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;

public abstract class TIntegerGenerator extends TObject {
    /**
     * Generate a new integer value.
     * @return the integer
     */
    public abstract TInteger generate();
}
