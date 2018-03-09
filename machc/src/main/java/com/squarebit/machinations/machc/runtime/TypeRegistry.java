package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;

import java.util.HashMap;
import java.util.Map;

/**
 * The type registry used by a Mach machine.
 */
final class TypeRegistry {
    private Map<String, TType> typeByName = new HashMap<>();

    /**
     * Instantiates a new type registry.
     */
    public TypeRegistry() {
        this.registerBuiltinTypes();
    }

    /**
     * Gets a type using its name.
     *
     * @param typeName the type name
     * @return the type
     */
    public TType getType(String typeName) {
        return typeByName.get(typeName);
    }


    /**
     * Register built-in types.
     */
    private void registerBuiltinTypes() {
        registerType(TType.OBJECT_TYPE);
        registerType(TType.GRAPH_TYPE);
    }

    /**
     * Register a built-in type with given implementing class.
     *
     * @param type the type
     * @param <T> implementing class type
     */
    public <T extends TObject> void registerType(TType<T> type) {
        typeByName.put(type.getName(), type);
    }
}
