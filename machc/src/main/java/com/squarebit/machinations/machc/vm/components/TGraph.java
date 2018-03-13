package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;

/**
 * The base graph type.
 */
public abstract class TGraph extends TObject {
    /**
     * Gets the type of this object.
     *
     * @return object type.
     */
    @Override
    public TType getType() {
        return Types.GRAPH_TYPE;
    }
}
