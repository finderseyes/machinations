package com.squarebit.machinations.machc.avm.expressions;

import java.util.HashSet;
import java.util.Set;

/**
 * An expression that evaluates to a {@link com.squarebit.machinations.machc.avm.runtime.TSetDescriptor}.
 */
public class SetDescriptor extends Expression {
    private Set<SetElementDescriptor> elementDescriptors = new HashSet<>();

    /**
     * Adds an element descriptor to the set descriptor.
     * @param descriptor the {@link SetElementDescriptor} instance
     */
    public void add(SetElementDescriptor descriptor) {
        this.elementDescriptors.add(descriptor);
    }

    /**
     * Gets element descriptors.
     *
     * @return the element descriptors
     */
    public Set<SetElementDescriptor> getElementDescriptors() {
        return this.elementDescriptors;
    }
}
