package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.exceptions.TypeAlreadyExistedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A module in an Abstract Virtual Machine (AVM).
 */
public final class ModuleInfo {
    private List<TypeInfo> types;
    private Map<String, TypeInfo> typeByName;

    /**
     * Instantiates a new object.
     */
    public ModuleInfo() {
        this.types = new ArrayList<>();
        this.typeByName = new HashMap<>();
    }

    /**
     * Gets list of types declared in this module.
     *
     * @return the type list in declaration order
     */
    public List<TypeInfo> getTypes() {
        return types;
    }

    /**
     * Creates a new {@link TypeInfo} in this module.
     *
     * @param typeName the type name
     * @return newly created {@link TypeInfo} instance
     * @throws TypeAlreadyExistedException if a TypeInfo with given name already existed in the module.
     */
    public TypeInfo createType(String typeName) throws TypeAlreadyExistedException {
        TypeInfo typeInfo = new TypeInfo().setModule(this).setName(typeName);
        addType(typeInfo);

        return typeInfo;
    }

    /**
     * Adds a {@link TypeInfo} to this module.
     *
     * @param typeInfo the instance to add
     * @throws TypeAlreadyExistedException if a TypeInfo with given name already existed in the module.
     */
    public void addType(TypeInfo typeInfo) throws TypeAlreadyExistedException {
        if (typeByName.containsKey(typeInfo.getName()))
            throw new TypeAlreadyExistedException(this, typeInfo.getName());

        typeInfo.setModule(this);
        types.add(typeInfo);
        typeByName.put(typeInfo.getName(), typeInfo);
    }

    /**
     * Finds a type with given name.
     *
     * @param name the type name
     * @return the {@link TypeInfo} instance
     */
    public TypeInfo findType(String name) {
        return typeByName.getOrDefault(name, null);
    }
}
