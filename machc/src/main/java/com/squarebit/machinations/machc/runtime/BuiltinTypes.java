package com.squarebit.machinations.machc.runtime;

public class BuiltinTypes {
    public static final TType OBJECT_TYPE = new TType("Object", null);
    public static final TType GRAPH_TYPE = new TType("Graph", OBJECT_TYPE);
    public static final TType DEFAULT_RESOURCE_TYPE = new TType("DefaultResource", OBJECT_TYPE);
    public static final TType SCALAR_RESOURCE_TYPE = new TType("ScalarResource", OBJECT_TYPE);

}
