package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.runtime.components.*;

public class BuiltinTypes {
    public static final TType<TObject> OBJECT_TYPE = new TType<>(null, TObject.class);
    public static final TType<TInteger> INTEGER_TYPE = new TType<>(OBJECT_TYPE, TInteger.class, true);
    public static final TType<TFloat> FLOAT_TYPE = new TType<>(OBJECT_TYPE, TFloat.class, true);

    public static final TType<TGraph> GRAPH_TYPE = new TType<>("Graph", OBJECT_TYPE, TGraph.class);

    public static final TType<TSet> SET_TYPE = new TType<>("Set", OBJECT_TYPE, TSet.class);

}
