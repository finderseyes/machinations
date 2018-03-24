package com.squarebit.machinations.machc.ast.expressions;

import java.util.LinkedList;
import java.util.List;

public final class GSetDescriptor implements GExpression {
    private List<GSetElementDescriptor> elementDescriptors = new LinkedList<>();

    public List<GSetElementDescriptor> getElementDescriptors() {
        return elementDescriptors;
    }

    public void addElementDescriptor(GSetElementDescriptor descriptor) {
        this.elementDescriptors.add(descriptor);
    }
}
