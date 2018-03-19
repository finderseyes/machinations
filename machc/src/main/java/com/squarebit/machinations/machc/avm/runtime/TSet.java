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

    /**
     * Instantiates a new T set.
     */
    public TSet() {
        this.elementsByType = new HashMap<>();
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
        Set<TSetElement> elements = elementsByType.computeIfAbsent(element.getTypeName(), n -> new HashSet<>());
        elements.add(element);
    }
}
