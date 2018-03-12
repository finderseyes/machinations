package com.squarebit.machinations.machc.runtime.expressions;

import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.components.TFloat;
import com.squarebit.machinations.machc.runtime.components.TInteger;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;

/**
 * The binary operator.
 */
public final class TAdd extends TExpression {
    private TExpression lhs, rhs;

    /**
     * Instantiates a new T add.
     *
     * @param lhs the lhs
     * @param rhs the rhs
     */
    public TAdd(TExpression lhs, TExpression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * Evaluates the expression to the given type.
     *
     * @param type       the requested type
     * @param activation the current frame activation
     * @return TObjectImpl corresponding to the result.
     */
    @Override
    public TObject evalTo(TType type, FrameActivation activation) {
        if (type == TType.INTEGER_TYPE || type == TType.FLOAT_TYPE) {
            TFloat lhsValue = (TFloat)lhs.evalTo(TType.FLOAT_TYPE, activation);
            TFloat rhsValue = (TFloat)rhs.evalTo(TType.FLOAT_TYPE, activation);

            if (type == TType.INTEGER_TYPE)
                return new TInteger((int)(lhsValue.getValue() + rhsValue.getValue()));
            else
                return new TFloat(lhsValue.getValue() + rhsValue.getValue());
        }
        else
            throw new RuntimeException("Cannot add given operands.");
    }
}
