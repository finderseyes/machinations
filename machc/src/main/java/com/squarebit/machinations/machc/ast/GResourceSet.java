package com.squarebit.machinations.machc.ast;

import java.util.HashMap;
import java.util.Map;

public class GResourceSet {
    private Map<String, GResourceDescriptor> descriptors = new HashMap<>();

    public GResourceDescriptor addDescriptor(GResourceDescriptor descriptor) {
        return descriptors.put(descriptor.getResourceName(), descriptor);
    }
}
