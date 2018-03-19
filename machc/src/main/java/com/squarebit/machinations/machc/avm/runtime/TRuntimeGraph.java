package com.squarebit.machinations.machc.avm.runtime;

public class TRuntimeGraph extends TGraph implements TSetElement {
    /**
     * Name of the element name.
     *
     * @return name of the element type
     */
    @Override
    public String getTypeName() {
        return getTypeInfo().getName();
    }
}
