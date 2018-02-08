package com.squarebit.machinations.specs.yaml;

/**
 * The type Element spec.
 */
public abstract class ElementSpec {
    private String id;

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     * @return the id
     */
    public ElementSpec setId(String id) {
        this.id = id;
        return this;
    }
}
