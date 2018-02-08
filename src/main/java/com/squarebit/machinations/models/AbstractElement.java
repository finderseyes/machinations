package com.squarebit.machinations.models;

public abstract class AbstractElement {
    private String id;

    public String getId() {
        return id;
    }

    public AbstractElement setId(String id) {
        this.id = id;
        return this;
    }
}
