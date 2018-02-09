package com.squarebit.machinations.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MachinationsContext {
    public static final String DEFAULT_RESOURCE_NAME = "";

    private Set<AbstractElement> elements;
    private Map<String, AbstractElement> elementById;

    public MachinationsContext() {
        this.elements = new HashSet<>();
        this.elementById = new HashMap<>();
    }

    /**
     * Find by id abstract element.
     *
     * @param id the id
     * @return the abstract element
     */
    public AbstractElement findById(String id) {
        return this.elementById.get(id);
    }

    /**
     * Gets elements.
     *
     * @return the elements
     */
    public Set<AbstractElement> getElements() {
        return elements;
    }

    /**
     * Addition element.
     *
     * @param element the element
     * @throws Exception the exception
     */
    protected void addElement(AbstractElement element) throws Exception {
        if (!this.elementById.containsKey(element.getId())) {
            this.elements.add(element);
            this.elementById.put(element.getId(), element);
        }
        else {
            throw new Exception(String.format("An element with id %s is already existed.", element.getId()));
        }
    }
}
