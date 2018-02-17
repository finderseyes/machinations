package com.squarebit.machinations.models;

import java.util.Map;
import java.util.Set;

public class Gate extends AbstractNode {
    private boolean random = false;

    public boolean isRandom() {
        return random;
    }

    public Gate setRandom(boolean random) {
        this.random = random;
        return this;
    }

    @Override
    public Set<ResourceConnection> activate(int time, Map<ResourceConnection, ResourceSet> incomingFlows) {
        if (this.isRandom()) {
            return super.activate(time, incomingFlows);
        }
        else {
            return super.activate(time, incomingFlows);
        }
    }
}
