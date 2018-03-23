package com.squarebit.machinations.machc.avm.runtime;

public class TSetElementTypeDescriptor {
    private TLambda size;
    private int capacity;
    private String name;

    private boolean isInstantiated = false;
    private int instantiatedSize = 0;

    /**
     * Instantiates a new T set element type descriptor.
     *
     * @param name the name
     */
    public TSetElementTypeDescriptor(String name) {
        this(null, -1, name);
    }

    /**
     * Instantiates a new T set element type descriptor.
     *
     * @param capacity the capacity
     * @param name     the name
     */
    public TSetElementTypeDescriptor(int capacity, String name) {
        this(null, capacity, name);
    }

    /**
     * Instantiates a new T set element type descriptor.
     *
     * @param size     the size
     * @param capacity the capacity
     * @param name     the name
     */
    public TSetElementTypeDescriptor(TLambda size, int capacity, String name) {
        this.size = size;
        this.capacity = capacity;
        this.name = name == null ? "" : name;
    }

    public TLambda getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    public boolean isInstantiated() {
        return isInstantiated;
    }

    public int getInstantiatedSize() {
        return instantiatedSize;
    }

    /**
     * Instantiate t set element type descriptor.
     *
     * @param instantiatedSize the instantiated size
     * @param capacity         the capacity
     * @param name             the name
     * @return the t set element type descriptor
     */
    public static TSetElementTypeDescriptor instantiate(int instantiatedSize, int capacity, String name) {
        TSetElementTypeDescriptor instance = new TSetElementTypeDescriptor(capacity, name);
        instance.isInstantiated = true;
        instance.instantiatedSize = instantiatedSize;
        return instance;
    }
}
