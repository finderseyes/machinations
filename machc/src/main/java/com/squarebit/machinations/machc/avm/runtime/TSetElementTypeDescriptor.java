package com.squarebit.machinations.machc.avm.runtime;

public class TSetElementTypeDescriptor {
    private int size;
    private int capacity;
    private String name;

    /**
     * Instantiates a new T set element type descriptor.
     *
     * @param name the name
     */
    public TSetElementTypeDescriptor(String name) {
        this(0, -1, name);
    }

    /**
     * Instantiates a new T set element type descriptor.
     *
     * @param capacity the capacity
     * @param name     the name
     */
    public TSetElementTypeDescriptor(int capacity, String name) {
        this(0, capacity, name);
    }

    /**
     * Instantiates a new T set element type descriptor.
     *
     * @param size     the size
     * @param capacity the capacity
     * @param name     the name
     */
    public TSetElementTypeDescriptor(int size, int capacity, String name) {
        this.size = size;
        this.capacity = capacity;
        this.name = name == null ? "" : name;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }
}
