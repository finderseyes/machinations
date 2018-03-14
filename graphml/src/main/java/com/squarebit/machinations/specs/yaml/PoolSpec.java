package com.squarebit.machinations.specs.yaml;

/**
 * The type Pool spec.
 */
public class PoolSpec extends NodeSpec {
    private String resources;
    private String capacity;

    public String getResources() {
        return resources;
    }

    public PoolSpec setResources(String resources) {
        this.resources = resources;
        return this;
    }

    public String getCapacity() {
        return capacity;
    }

    public PoolSpec setCapacity(String capacity) {
        this.capacity = capacity;
        return this;
    }
}
