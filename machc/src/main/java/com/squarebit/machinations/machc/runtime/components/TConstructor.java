package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.runtime.TConstructorScope;

public final class TConstructor<T extends TObject> extends TExecutable {
    TType<T> declaringType;

    public T newInstance() throws InstantiationException, IllegalAccessException {
        // 1. create a new "raw" instance.
        T instance = declaringType.newInstance();



        TConstructorScope scope = new TConstructorScope().setThisObject(instance);


        // 2. set the current scope of execution.
        // TScope scope =
        // 3. execute the instructions in the scope.
        return null;
    }
}
