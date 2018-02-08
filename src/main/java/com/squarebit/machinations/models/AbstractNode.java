package com.squarebit.machinations.models;

public abstract class AbstractNode extends AbstractElement {
    private String name;
    private ActivationMode activationMode;

    public String getName() {
        return name;
    }

    public AbstractNode setName(String name) {
        this.name = name;
        return this;
    }

    public ActivationMode getActivationMode() {
        return activationMode;
    }

    public AbstractNode setActivationMode(ActivationMode activationMode) {
        this.activationMode = activationMode;
        return this;
    }
}
