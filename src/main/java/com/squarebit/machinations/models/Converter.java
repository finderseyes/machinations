package com.squarebit.machinations.models;

import java.util.Map;
import java.util.Set;

public class Converter extends Pool {
    @Override
    public Set<ResourceConnection> __activate(int time, Map<ResourceConnection, ResourceSet> incomingFlows) {
        incomingFlows.forEach((c, a) -> c.getFrom().extract(a));

        this.getOutgoingConnections().forEach(c -> {
            int rate = c.getFlowRateValue();
            c.getTo().receive(ResourceSet.of(c.getResourceName(), rate));
        });

        return this.getOutgoingConnections();
    }
}
