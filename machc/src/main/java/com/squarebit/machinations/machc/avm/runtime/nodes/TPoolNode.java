package com.squarebit.machinations.machc.avm.runtime.nodes;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.Machine;
import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.runtime.TNode;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.TSet;
import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;

import java.util.concurrent.CompletableFuture;

public class TPoolNode extends TNode {
    private TSet content;

    /**
     * Init completable future.
     *
     * @return the completable future
     */
    @ConstructorMethod
    public CompletableFuture<TObject> init(Machine machine) {
        this.content = new TSet();  // Empty set.
        return CompletableFuture.completedFuture(this);
    }

    /**
     * Init completable future.
     *
     * @param set the set
     * @return the completable future
     */
    @ConstructorMethod
    public CompletableFuture<TObject> init(Machine machine, TSet set) {
        this.content = set;
        return CompletableFuture.completedFuture(this);
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.POOL_NODE_TYPE;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public TSet getContent() {
        return content;
    }
}
