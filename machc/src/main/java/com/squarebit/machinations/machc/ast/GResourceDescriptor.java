package com.squarebit.machinations.machc.ast;

/**
 * The resource descriptor.
 */
public final class GResourceDescriptor {
    private String name = "";
    private int capacity = -1;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     */
    public GResourceDescriptor setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets capacity.
     *
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets capacity.
     *
     * @param capacity the capacity
     * @return the capacity
     */
    public GResourceDescriptor setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }
}
