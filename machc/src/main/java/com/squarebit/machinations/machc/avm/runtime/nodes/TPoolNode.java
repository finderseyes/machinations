package com.squarebit.machinations.machc.avm.runtime.nodes;

import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.runtime.TNode;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.TSetDescriptor;
import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;

import java.util.concurrent.CompletableFuture;

public class TPoolNode extends TNode {
    @ConstructorMethod
    public CompletableFuture<TObject> init() {
        return CompletableFuture.completedFuture(null);
    }

    @ConstructorMethod
    public CompletableFuture<TObject> init(TSetDescriptor descriptor) {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return null;
    }
}
