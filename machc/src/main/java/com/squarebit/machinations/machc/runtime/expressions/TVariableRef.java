package com.squarebit.machinations.machc.runtime.expressions;

import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.Variable;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;

public final class TVariableRef extends TExpression {
    private final Variable variable;

    /**
     * Instantiates a new T variable ref.
     *
     * @param variable the variable
     */
    public TVariableRef(Variable variable) {
        this.variable = variable;
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
        TObject value = variable.get(activation);
        return TExpression.evalTo(type, value);
    }
}
