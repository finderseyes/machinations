package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.runtime.TRuntimeGraph;

/**
 * The type Graph type info.
 */
public class GraphTypeInfo extends TypeInfo {

    /**
     * Instantiates a new Graph type info.
     */
    public GraphTypeInfo() {
        this.setImplementingClass(TRuntimeGraph.class);
    }

    /**
     * Gets implementing class.
     *
     * @return the implementing class
     */
    @Override
    public Class getImplementingClass() {
        return super.getImplementingClass();
    }

    /**
     * Sets implementing class.
     *
     * @param implementingClass the implementing class
     * @return the implementing class
     */
    @Override
    public TypeInfo setImplementingClass(Class implementingClass) {
        if (implementingClass != TRuntimeGraph.class)
            throw new RuntimeException("Invalid operation");

        return super.setImplementingClass(implementingClass);
    }
}
