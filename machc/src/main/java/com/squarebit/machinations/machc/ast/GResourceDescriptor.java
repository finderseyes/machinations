package com.squarebit.machinations.machc.ast;

/**
 * The resource descriptor.
 */
public final class GResourceDescriptor {
    private String resourceName = "";
    private GExpression size;
    private int capacity = -1;

    /**
     * Gets resource name.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Sets resource name.
     *
     * @param resourceName the resource name
     * @return the resource name
     */
    public GResourceDescriptor setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public GExpression getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     * @return the size
     */
    public GResourceDescriptor setSize(GExpression size) {
        this.size = size;
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
