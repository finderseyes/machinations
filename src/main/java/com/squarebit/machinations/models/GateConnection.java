package com.squarebit.machinations.models;

/**
 * The connection from a gate to another node.
 */
public class GateConnection extends ResourceConnection {
    @Override
    public ResourceSet activate() {
        return ResourceSet.empty();
    }
}
