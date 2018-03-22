package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.Machine;
import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;

import java.util.concurrent.CompletableFuture;

public class TConnection implements TObject {
    TGraph graph;
    private TExpression flow;
    private TNode from;
    private TNode to;

    /**
     *
     * @param machine
     * @param from
     * @param to
     * @return
     */
    @ConstructorMethod
    public CompletableFuture<TObject> init(Machine machine, TNode from, TNode to, TExpression flow) {
        this.from = from;
        this.to = to;
        this.flow = flow;

        return CompletableFuture.completedFuture(this);
    }

    /**
     * Gets graph.
     *
     * @return the graph
     */
    public TGraph getGraph() {
        return graph;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public TNode getFrom() {
        return from;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public TNode getTo() {
        return to;
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.CONNECTION_TYPE;
    }
}
