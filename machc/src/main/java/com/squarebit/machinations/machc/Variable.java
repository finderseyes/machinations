package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.runtime.components.TType;

/**
 * A local variable declared in or argument passed to a method.
 */
public final class Variable {
    private String name;
    private TType type = TType.OBJECT_TYPE;
}
