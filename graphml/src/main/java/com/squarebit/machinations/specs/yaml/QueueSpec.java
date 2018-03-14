package com.squarebit.machinations.specs.yaml;

/**
 * The type Queue spec.
 */
public class QueueSpec extends NodeSpec {
    private String delay;

    public String getDelay() {
        return delay;
    }

    public QueueSpec setDelay(String delay) {
        this.delay = delay;
        return this;
    }
}
