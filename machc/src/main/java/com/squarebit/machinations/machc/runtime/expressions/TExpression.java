package com.squarebit.machinations.machc.runtime.expressions;

import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;

/**
 * An Mach-machine expression, whose operands include literals and stack direct access.
 */
public abstract class TExpression {

    /**
     * Evaluates the expression to the given type.
     *
     * @param type       the requested type
     * @param activation the current frame activation
     * @return TObject corresponding to the result.
     */
    public abstract TObject evalTo(TType type, FrameActivation activation);

    /**
     * Evaluates a TObject to given type.
     *
     * @param type  the requested type
     * @param value the value to evaluated
     * @return the result
     */
    public static TObject evalTo(TType type, TObject value) {
        if (value == null)
            return null;

        if (value.getType() == type)
            return value;

        return null;
    }
}
