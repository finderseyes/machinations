package com.squarebit.machinations.models;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNode extends AbstractElement {
    protected String name;
    protected ActivationMode activationMode = ActivationMode.PASSIVE;

    private HashSet<AbstractConnection> outgoingConnections = new HashSet<>();
    private HashSet<AbstractConnection> incomingConnections = new HashSet<>();
    private HashSet<Modifier> modifiers = new HashSet<>();
    private HashSet<Trigger> triggers = new HashSet<>();
    private HashSet<Activator> activators = new HashSet<>();

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param value the value
     */
    protected AbstractNode setName(String value) {
        this.name = value;
        return this;
    }

    /**
     * Gets activation mode.
     *
     * @return the activation mode
     */
    public ActivationMode getActivationMode() {
        return activationMode;
    }

    /**
     * Sets activation mode.
     *
     * @param activationMode the activation mode
     * @return the activation mode
     */
    protected AbstractNode setActivationMode(ActivationMode activationMode) {
        this.activationMode = activationMode;
        return this;
    }

    public Set<AbstractConnection> getOutgoingConnections() {
        return this.outgoingConnections;
    }

    public Set<AbstractConnection> getIncomingConnections() {
        return this.incomingConnections;
    }

    public HashSet<Modifier> getModifiers() {
        return modifiers;
    }

    public HashSet<Trigger> getTriggers() {
        return triggers;
    }

    public HashSet<Activator> getActivators() {
        return activators;
    }

    /**
     * Instantiates a new Abstract vertex.
     */
    public AbstractNode() {

    }
}
