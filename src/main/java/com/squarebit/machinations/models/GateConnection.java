package com.squarebit.machinations.models;

/**
 * The connection from a gate to another node.
 */
public class GateConnection extends ResourceConnection {
    @Override
    public ResourceSet activate() {
        // A gate (resource) connection does not return anything upon activation.
        return ResourceSet.empty();
    }
}
