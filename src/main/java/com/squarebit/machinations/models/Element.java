package com.squarebit.machinations.models;

/**
 * The type Abstract element.
 */
public abstract class Element {
    protected Machinations machinations;
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
    public Element setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Gets machinations.
     *
     * @return the machinations
     */
    public Machinations getMachinations() {
        return machinations;
    }
}
