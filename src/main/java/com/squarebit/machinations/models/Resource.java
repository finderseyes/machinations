package com.squarebit.machinations.models;

public class Resource {
    public static final Resource DEFAULT = new Resource();

    private final String name;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Instantiates a new Resource.
     */
    public Resource() {
        this.name = "";
    }

    /**
     * Instantiates a new Resource.
     *
     * @param name the name
     */
    public Resource(String name) {
        this.name = name;
    }
}
