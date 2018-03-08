package com.squarebit.machinations.machc.runtime.expressions;

import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;

/**
 * An expression evaluates by referencing to a TObject.
 */
public final class TObjectRef extends TExpression {
    private final TObject value;

    /**
     * Instantiates a new T object ref.
     *
     * @param value the value
     */
    public TObjectRef(TObject value) {
        this.value = value;
    }

    /**
     * Evaluates the expression to the given type.
     *
     * @param type       the requested type
     * @param activation the current frame activation
     * @return TObject corresponding to the result.
     */
    @Override
    public TObject evalTo(TType type, FrameActivation activation) {
        return TExpression.evalTo(type, value);
    }
}
