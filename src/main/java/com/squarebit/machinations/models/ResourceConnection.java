package com.squarebit.machinations.models;

import java.util.function.Supplier;

public class ResourceConnection extends AbstractConnection {
    private Supplier<Integer> rateFunction;

    public ResourceConnection() {
        this.rateFunction = () -> 1;
    }

    /**
     * Gets rate.
     *
     * @return the rate
     */
    public int getRate() {
        if (rateFunction != null)
            return rateFunction.get();
        else
            return 1;
    }

    /**
     * Sets rate.
     *
     * @param value the value
     * @return the rate
     */
    public ResourceConnection setRate(int value) {
        this.rateFunction = () -> value;
        return this;
    }

    /**
     * Sets rate.
     *
     * @param rateFunction the rate function
     * @return the rate
     */
    public ResourceConnection setRate(Supplier<Integer> rateFunction) {
        this.rateFunction = rateFunction;
        return this;
    }
}
