package com.squarebit.machinations.specs.yaml;

/**
 * The type Gate spec.
 */
public class GateSpec extends NodeSpec {
    private boolean random = false;

    public boolean isRandom() {
        return random;
    }

    public GateSpec setRandom(boolean random) {
        this.random = random;
        return this;
    }
}
