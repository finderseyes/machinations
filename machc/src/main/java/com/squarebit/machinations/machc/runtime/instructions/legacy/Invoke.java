package com.squarebit.machinations.machc.runtime.instructions.legacy;

import com.squarebit.machinations.machc.runtime.Frame;
import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.Instruction;
import com.squarebit.machinations.machc.runtime.Variable;
import com.squarebit.machinations.machc.runtime.components.TInteger;
import com.squarebit.machinations.machc.runtime.components.TMethod;
import com.squarebit.machinations.machc.runtime.components.TObject;

public final class Invoke extends Instruction {
    private final TMethod method;
    private final Variable instance;
    private final Variable[] paramenters;

    /**
     * Instantiates a new method call.
     *
     * @param method the method
     */
    public Invoke(TMethod method, Variable instance, Variable[] parameters) {
        this.method = method;
        this.instance = instance;
        this.paramenters = parameters;
    }

    /**
     * Execute the current instruction.
     */
    @Override
    public void execute() throws Exception {
        Frame frame = this.getFrame();
        FrameActivation activation = frame.currentActivation();

//        if (method.isNativeMethod()) {
//            Object[] _parameters = Stream.of(paramenters).map(p -> p.get(activation)).toArray(Object[]::new);
//
//            Object returnValue = method.getNativeMethod().invoke(instance.get(activation), _parameters);
//            TObject wrappedValue = wrapNative(returnValue);
//
//            frame.getMethodReturnValue().set(activation, wrappedValue);
//        }
    }

    private TObject wrapNative(Object value) {
        if (value instanceof Integer)
            return new TInteger((int)value);

        return null;
    }
}
