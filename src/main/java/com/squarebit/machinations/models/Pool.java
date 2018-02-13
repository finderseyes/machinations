package com.squarebit.machinations.models;

import java.util.Collections;
import java.util.Map;

public class Pool extends AbstractNode {
    @Override
    public ActivationRequirement getActivationRequirement() {
        if (isPulling()) {
            if (isAllOrNoneFlow())
                return ActivationRequirement.all(this, this.getIncomingConnections());
            else
                return ActivationRequirement.any(this, this.getIncomingConnections());
        }
        else
            return ActivationRequirement.any(this, Collections.emptySet());
    }

    @Override
    public ResourceSet getResources() {
        return this.resources;
    }

    @Override
    public void activate(int time, Map<ResourceConnection, ResourceSet> incomingFlows) {
        if (isPulling()) {
            incomingFlows.forEach((c, a) -> {
                c.getFrom().extract(a);
                this.receive(a);
            });
        }
        else {
            this.getOutgoingConnections().forEach(c -> {
                int rate = c.getFlowRate();
                ResourceSet extracted = this.resources.extract(rate);
                c.getTo().receive(extracted);
            });
        }
    }

    @Override
    public ResourceSet extract(ResourceSet resourceSet) {
        return resources.extract(resourceSet.size());
    }

    @Override
    public ResourceSet extractExact(ResourceSet resourceSet) {
        return extract(resourceSet);
    }

    @Override
    public boolean receive(ResourceSet resourceSet) {
        resources.add(resourceSet);
        return true;
    }
}
