package com.squarebit.machinations.machc.ast;

import java.util.HashMap;
import java.util.Map;

/**
 * A graph.
 */
public class GGraph extends GObject {
    private Map<String, GGraphElement> elements = new HashMap<>();  // Mapping from id to graph element.

    /**
     * Finds a graph element with given id.
     *
     * @param id the id
     * @return the graph element
     */
    public GGraphElement findElement(String id) {
        return elements.get(id);
    }

    /**
     * Adds a graph element.
     *
     * @param element the element
     * @return the graph element
     */
    public GGraphElement addElement(GGraphElement element) {
        return elements.put(element.getId(), element);
    }
}
