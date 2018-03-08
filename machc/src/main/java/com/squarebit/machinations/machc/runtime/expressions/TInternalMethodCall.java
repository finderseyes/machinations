package com.squarebit.machinations.machc.runtime.expressions;

import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;

import java.lang.reflect.Method;
import java.util.function.Function;

public class TInternalMethodCall extends TExpression {
    private TType type;
    private Method method;

    public TInternalMethodCall(TType type, String name, Class... parameterTypes)
            throws NoSuchMethodException, SecurityException {
        this.type = type;

        Class implementation = type.getImplementation();
        this.method = implementation.getMethod(name, parameterTypes);
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
        return null;
    }
}
