package com.squarebit.machinations.models;

import java.util.HashSet;
import java.util.Set;

/**
 * The base class of graph elements such as nodes and connections.
 */
public abstract class GraphElement extends Element {
    private Set<Modifier> modifiedBy = new HashSet<>();

    /**
     * Gets the modifiers affecting this element.
     *
     * @return the modifier set
     */
    public Set<Modifier> getModifiedBy() {
        return modifiedBy;
    }
}
