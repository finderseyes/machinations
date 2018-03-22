package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.*;
import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;
import com.squarebit.machinations.machc.avm.runtime.annotations.NativeField;

import java.util.concurrent.CompletableFuture;

public class TLambda implements TObject {
    private LambdaTypeInfo __typeInfo;

    @NativeField(name = "arguments")
    public TArray arguments;

    @ConstructorMethod
    public CompletableFuture<TObject> init(Machine machine, TArray arguments) {
        this.arguments = arguments;
        return CompletableFuture.completedFuture(this);
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return __typeInfo;
    }
}
