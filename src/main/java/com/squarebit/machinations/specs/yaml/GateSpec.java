package com.squarebit.machinations.specs.yaml;

/**
 * The type Gate spec.
 */
public class GateSpec extends NodeSpec {
    private boolean random = false;
    private String draw = null;

    public boolean isRandom() {
        return random;
    }

    public GateSpec setRandom(boolean random) {
        this.random = random;
        return this;
    }

    public String getDraw() {
        return draw;
    }

    public GateSpec setDraw(String draw) {
        this.draw = draw;
        return this;
    }
}
