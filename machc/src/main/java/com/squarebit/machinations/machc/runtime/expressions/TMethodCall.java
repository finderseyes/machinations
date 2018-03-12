package com.squarebit.machinations.machc.runtime.expressions;

import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;

public class TMethodCall extends TExpression {
    /**
     * Evaluates the expression to the given type.
     *
     * @param type       the requested type
     * @param activation the current frame activation
     * @return TObjectImpl corresponding to the result.
     */
    @Override
    public TObject evalTo(TType type, FrameActivation activation) {
        return null;
    }
}
