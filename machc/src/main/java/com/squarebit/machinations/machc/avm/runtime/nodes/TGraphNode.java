package com.squarebit.machinations.machc.avm.runtime.nodes;

import com.squarebit.machinations.machc.avm.GraphNodeTypeInfo;
import com.squarebit.machinations.machc.avm.Machine;
import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.runtime.TNode;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.TRuntimeGraph;
import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;

import java.util.concurrent.CompletableFuture;

public class TGraphNode extends TNode {
    // Internal
    private GraphNodeTypeInfo __typeInfo;


    private Machine machine;
    private TRuntimeGraph graph;


    @ConstructorMethod
    public CompletableFuture<TObject> init(Machine machine) {
        this.machine = machine;

        TypeInfo graphType = __typeInfo.getGraphType();

        CompletableFuture<TObject> returnFuture = machine.newInstance(graphType);
        returnFuture.thenAccept(v -> graph = (TRuntimeGraph)v);

        return returnFuture;
    }

    public TRuntimeGraph getGraph() {
        return graph;
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public GraphNodeTypeInfo getTypeInfo() {
        return __typeInfo;
    }
}
