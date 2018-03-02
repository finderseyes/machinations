package com.squarebit.machinations.models;

import java.util.HashSet;
import java.util.Set;

public class Converter extends Pool {
    /**
     * Activates a node, giving incoming resource flow.
     *
     * @param incomingResources incoming resources
     */
    @Override
    public Set<ResourceConnection> fire(ResourceSet incomingResources) {
        this.getOutgoingConnections().forEach(c -> {
            ResourceSet requiredResources = c.fire();
            c.getTo().receive(requiredResources);
        });

        return this.getOutgoingConnections();
    }

    @Override
    public FireRequirement getFireRequirement() {
        return FireRequirement.all(this, this.getIncomingConnections());
    }
}
