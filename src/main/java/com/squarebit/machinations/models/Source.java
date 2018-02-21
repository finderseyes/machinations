package com.squarebit.machinations.models;

/**
 * The type Source.
 */
public class Source extends Pool {
    public Source() {
        this.resources = ResourceSet.infinite();
    }

    @Override
    public ResourceSet extract(ResourceSet resourceSet) {
        return resourceSet.copy();
    }
}
