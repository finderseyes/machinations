package com.squarebit.machinations.models;

/**
 * The type Abstract element.
 */
public abstract class AbstractElement {
    protected MachinationsContext machinations;
    protected int lastActivatedTime = -1;
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

    /**
     * Gets last activated time.
     *
     * @return the last activated time
     */
    public int getLastActivatedTime() {
        return lastActivatedTime;
    }

    /**
     * Activate.
     *
     * @param time the time
     */
    public final void activate(int time) {
        this.doActivate(time);
        this.lastActivatedTime = time;
    }

    /**
     * Do pre activate.
     *
     * @param time the time
     */
    protected void doPreActivate(int time) {}

    /**
     * Do activate.
     *
     * @param time the time
     */
    protected void doActivate(int time) {}

    /**
     * Do post activate.
     *
     * @param time the time
     */
    protected void doPostActivate(int time) {}
}
