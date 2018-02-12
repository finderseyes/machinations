package com.squarebit.machinations.models;

public class Configs {
    private TimeMode timeMode = TimeMode.ASYNCHRONOUS;
    private int actionPointsPerTurn = 1;

    public TimeMode getTimeMode() {
        return timeMode;
    }

    public Configs setTimeMode(TimeMode timeMode) {
        this.timeMode = timeMode;
        return this;
    }

    public int getActionPointsPerTurn() {
        return actionPointsPerTurn;
    }

    public Configs setActionPointsPerTurn(int actionPointsPerTurn) {
        this.actionPointsPerTurn = actionPointsPerTurn;
        return this;
    }
}
