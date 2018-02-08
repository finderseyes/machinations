package com.squarebit.machinations.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MachinationContext {
    private Set<AbstractElement> elements;
    private Map<String, AbstractElement> elementById;

    public MachinationContext() {
        this.elements = new HashSet<>();
        this.elementById = new HashMap<>();
    }

    public AbstractElement findById(String id) {
        return this.elementById.get(id);
    }

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
