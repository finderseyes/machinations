package com.squarebit.machinations.models;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNode extends AbstractElement {
    private String name;
    private ActivationMode activationMode;
    private FlowMode flowMode;
    private Set<AbstractConnection> incomingConnections = new HashSet<>();
    private Set<AbstractConnection> outgoingConnections = new HashSet<>();

    public String getName() {
        return name;
    }

    public AbstractNode setName(String name) {
        this.name = name;
        return this;
    }

    public ActivationMode getActivationMode() {
        return activationMode;
    }

    public AbstractNode setActivationMode(ActivationMode activationMode) {
        this.activationMode = activationMode;
        return this;
    }

    public FlowMode getFlowMode() {
        return flowMode;
    }

    public AbstractNode setFlowMode(FlowMode flowMode) {
        this.flowMode = flowMode;
        return this;
    }

    public Set<AbstractConnection> getIncomingConnections() {
        return incomingConnections;
    }

    public Set<AbstractConnection> getOutgoingConnections() {
        return outgoingConnections;
    }
}
