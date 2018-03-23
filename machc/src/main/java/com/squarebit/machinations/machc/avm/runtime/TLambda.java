package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.*;
import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;
import com.squarebit.machinations.machc.avm.runtime.annotations.NativeField;

import java.util.concurrent.CompletableFuture;

public class TLambda extends TObjectBase {
    @ConstructorMethod
    public CompletableFuture<TObject> init(Machine machine, TArray arguments) {
        setArguments(arguments);
        return CompletableFuture.completedFuture(this);
    }

    /**
     * Gets lambda type info.
     *
     * @return the lambda type info
     */
    public LambdaTypeInfo getLambdaTypeInfo() {
        return (LambdaTypeInfo)this.getTypeInfo();
    }

    /**
     * Gets arguments.
     *
     * @return the arguments
     */
    public TArray getArguments() {
        return (TArray)getField(getLambdaTypeInfo().getArgumentsField());
    }

    /**
     * Sets arguments.
     *
     * @param arguments the arguments
     * @return the arguments
     */
    public TLambda setArguments(TArray arguments) {
        setField(getLambdaTypeInfo().getArgumentsField(), arguments);
        return this;
    }
}
