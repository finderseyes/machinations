package com.squarebit.machinations.machc.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * The type registry used by a Mach machine.
 */
final class TypeRegistry {
    private Map<String, TType> typeByName = new HashMap<>();

    private void registerBuiltinTypes() {
        registerBuiltinType(BuiltinTypes.OBJECT_TYPE, TObject.class);
        registerBuiltinType(BuiltinTypes.GRAPH_TYPE, TGraph.class);
    }

    private <T extends TObject> void registerBuiltinType(TType type, Class<T> implementation) {

    }
}
