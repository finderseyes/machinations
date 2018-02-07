package com.squarebit.machinations.models;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractElement {
    private Set<Modifier> modifyingElements = new HashSet<>();

    public Set<Modifier> getModifyingElements() {
        return modifyingElements;
    }
}
