package com.squarebit.machinations.models;

public abstract class AbstractElement {
    protected MachinationsContext machinations;
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
    public AbstractElement setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Gets machinations.
     *
     * @return the machinations
     */
    public MachinationsContext getMachinations() {
        return machinations;
    }
}
