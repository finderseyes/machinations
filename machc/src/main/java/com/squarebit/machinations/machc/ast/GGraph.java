package com.squarebit.machinations.machc.ast;

import java.util.*;

/**
 * A graph.
 */
public class GGraph extends GSymbol {
    private boolean defaultGraph = false;
    private List<GGraphField> fields = new ArrayList<>();  // Mapping from id to graph field declaration.
    private List<GMethod> methods = new ArrayList<>(); // Mapping from function name to instance.

    /**
     * Is default graph boolean.
     *
     * @return the boolean
     */
    public boolean isDefaultGraph() {
        return defaultGraph;
    }

    /**
     * Sets default graph.
     *
     * @param defaultGraph the default graph
     * @return the default graph
     */
    public GGraph setDefaultGraph(boolean defaultGraph) {
        this.defaultGraph = defaultGraph;
        return this;
    }

    /**
     * Adds a graph field.
     *
     * @param field the field
     * @return the graph field
     */
    public void addField(GGraphField field) {
        fields.add(field);
    }

    /**
     * Gets fields.
     *
     * @return the fields
     */
    public List<GGraphField> getFields() {
        return fields;
    }



    /**
     * Adds a method to the graph.
     *
     * @param method the method
     * @return the method
     */
    public void addMethod(GMethod method) {
        methods.add(method);
    }

    /**
     * Gets methods.
     *
     * @return the methods
     */
    public List<GMethod> getMethods() {
        return methods;
    }
}
