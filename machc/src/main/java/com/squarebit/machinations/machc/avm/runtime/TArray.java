package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.Machine;
import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;

import java.util.concurrent.CompletableFuture;

public class TArray implements TObject {
    //////////////
    // Internal
    private TypeInfo __typeInfo;

    ///

    private TObject[] data;

    @ConstructorMethod
    public CompletableFuture<TObject> init(Machine machine, TInteger length) {
        this.data = new TObject[length.getValue()];
        return CompletableFuture.completedFuture(this);
    }

    /**
     * Get t object.
     *
     * @param index the index
     * @return the t object
     */
    public TObject get(int index) {
        return data[index];
    }

    /**
     * Set t array.
     *
     * @param index the index
     * @param value the value
     * @return the t array
     */
    public TArray set(int index, TObject value) {
        data[index] = value;
        return this;
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
