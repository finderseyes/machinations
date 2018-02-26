package com.squarebit.machinations.models;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class Pool extends Node {
    @Override
    public FireRequirement getFireRequirement() {
        if (isPulling()) {
            if (isAllOrNoneFlow())
                return FireRequirement.all(this, this.getIncomingConnections());
            else
                return FireRequirement.any(this, this.getIncomingConnections());
        }
        else
            return FireRequirement.any(this, Collections.emptySet());
    }

    @Override
    public ResourceSet getResources() {
        return this.resources;
    }

    @Override
    public Set<ResourceConnection> fire(int time, Map<ResourceConnection, ResourceSet> incomingFlows) {
        if (isPulling()) {
            incomingFlows.forEach((c, a) -> {
                c.getFrom().extract(a);
                this.receive(a);
            });

            return Collections.emptySet();
        }
        else {
            this.getOutgoingConnections().forEach(c -> {
                int rate = c.getFlowRateValue();
                ResourceSet extracted = this.extract(ResourceSet.of(rate));
                c.getTo().receive(extracted);
            });

            return this.getOutgoingConnections();
        }
    }

    @Override
    public ResourceSet extract(ResourceSet resourceSet) {
        return resources.remove(resourceSet.size());
    }

    @Override
    public boolean receive(ResourceSet resourceSet) {
        resources.add(resourceSet);
        return true;
    }
}
