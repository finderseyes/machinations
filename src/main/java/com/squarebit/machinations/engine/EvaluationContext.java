package com.squarebit.machinations.engine;

public class EvaluationContext {
    private boolean forceDeterministic = false;

    public boolean doesForceDeterministic() {
        return forceDeterministic;
    }

    public EvaluationContext setForceDeterministic(boolean forceDeterministic) {
        this.forceDeterministic = forceDeterministic;
        return this;
    }
}
