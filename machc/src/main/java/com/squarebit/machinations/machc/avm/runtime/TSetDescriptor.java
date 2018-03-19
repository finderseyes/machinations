package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class TSetDescriptor implements TObject {
    private Map<Object, TSetElementTypeDescriptor> typeDescriptorByName = new HashMap<>();

    /**
     * Instantiates a new T set descriptor.
     */
    public TSetDescriptor() {
    }

    /**
     * Instantiates a new T set descriptor.
     *
     * @param typeDescriptors the type descriptors
     */
    public TSetDescriptor(Collection<TSetElementTypeDescriptor> typeDescriptors) {
        for (TSetElementTypeDescriptor typeDescriptor: typeDescriptors) {
            typeDescriptorByName.put(typeDescriptor.getName(), typeDescriptor);
        }
    }

    @ConstructorMethod
    public CompletableFuture<TObject> init() {
        // int k = 10;
        // throw new RuntimeException("SDfsd");
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.SET_DESCRIPTOR_TYPE;
    }

    /**
     * Find element type descriptor t set element type descriptor.
     *
     * @param name the name
     * @return the t set element type descriptor
     */
    public TSetElementTypeDescriptor findElementTypeDescriptor(String name) {
        return typeDescriptorByName.getOrDefault(name, null);
    }

    /**
     * Gets element type descriptors.
     *
     * @return the element type descriptors
     */
    public Collection<TSetElementTypeDescriptor> getElementTypeDescriptors() {
        return typeDescriptorByName.values();
    }
}
