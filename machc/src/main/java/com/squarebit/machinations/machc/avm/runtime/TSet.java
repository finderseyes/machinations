package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class TSet implements TObject {
    private Map<String, Set<TSetElement>> elementsByType;
    private Set<TSetElement> elements;

    /**
     * Instantiates a new T set.
     */
    public TSet() {
        this.elementsByType = new HashMap<>();
        this.elements = new HashSet<>();
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.SET_TYPE;
    }

    /**
     * Adds an element to the set.
     * @param element element
     */
    public void add(TSetElement element) {
        Set<TSetElement> typeElements = elementsByType.computeIfAbsent(element.getTypeName(), n -> new HashSet<>());
        typeElements.add(element);

        this.elements.add(element);
    }

    /**
     * Gets the total size of the set.
     *
     * @return the size
     */
    public int size() {
        return this.elements.size();
    }

    /**
     * Gets the number of items of a given type.
     * @param typeName the type name.
     * @return number of items of given type.
     */
    public int size(String typeName) {
        Set<TSetElement> typeElements = elementsByType.getOrDefault(typeName, null);

        return (typeElements != null) ? typeElements.size() : 0;
    }
}