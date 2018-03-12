package com.squarebit.machinations.machc.ast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A graph.
 */
public class GGraph extends GSymbol {
    private Map<String, GGraphField> fields = new HashMap<>();  // Mapping from id to graph field declaration.
    private Map<String, GMethod> methods = new HashMap<>(); // Mapping from function name to instance.

    /**
     * Finds a field declared in the graph.
     *
     * @param id the field id (name)
     * @return the graph field, otherwise null.
     */
    public GGraphField findField(String id) {
        return fields.get(id);
    }

    /**
     * Adds a graph field.
     *
     * @param field the field
     * @return the graph field
     */
    public GGraphField addField(GGraphField field) {
        return fields.put(field.getName(), field);
    }

    /**
     * Gets fields.
     *
     * @return the fields
     */
    public Collection<GGraphField> getFields() {
        return fields.values();
    }

    /**
     * Finds a method with given name.
     *
     * @param id the method id (name)
     * @return the method
     */
    public GMethod findMethod(String id) {
        return methods.get(id);
    }

    /**
     * Adds a method to the graph.
     *
     * @param method the method
     * @return the method
     */
    public GMethod addMethod(GMethod method) {
        return methods.put(method.getName(), method);
    }

    /**
     * Gets methods.
     *
     * @return the methods
     */
    public Collection<GMethod> getMethods() {
        return methods.values();
    }
}
