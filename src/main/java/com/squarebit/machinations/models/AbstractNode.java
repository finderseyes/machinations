package com.squarebit.machinations.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractNode extends AbstractElement {
    private String name;
    private ActivationMode activationMode;
    private FlowMode flowMode;
    private Set<AbstractConnection> incomingConnections = new HashSet<>();
    private Set<AbstractConnection> outgoingConnections = new HashSet<>();
    private Map<String, Integer> resources = new HashMap<>();
    private Map<String, Integer> capacity = new HashMap<>();

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
     * @param name the name
     * @return the name
     */
    public AbstractNode setName(String name) {
        this.name = name;
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
    public AbstractNode setActivationMode(ActivationMode activationMode) {
        this.activationMode = activationMode;
        return this;
    }

    /**
     * Gets flow mode.
     *
     * @return the flow mode
     */
    public FlowMode getFlowMode() {
        return flowMode;
    }

    /**
     * Sets flow mode.
     *
     * @param flowMode the flow mode
     * @return the flow mode
     */
    public AbstractNode setFlowMode(FlowMode flowMode) {
        this.flowMode = flowMode;
        return this;
    }

    /**
     * Gets incoming connections.
     *
     * @return the incoming connections
     */
    public Set<AbstractConnection> getIncomingConnections() {
        return incomingConnections;
    }

    /**
     * Gets outgoing connections.
     *
     * @return the outgoing connections
     */
    public Set<AbstractConnection> getOutgoingConnections() {
        return outgoingConnections;
    }

    /**
     * Gets resources.
     *
     * @return the resources
     */
    public Map<String, Integer> getResources() {
        return resources;
    }

    /**
     * Gets resource count by name.
     *
     * @param name the name
     * @return the resource
     */
    public int getResourceCount(String name) {
        return resources.getOrDefault(name, 0);
    }

    /**
     *
     * @return
     */
    public int getTotalResourceCount() {
        return resources.values().stream().mapToInt(i -> i).sum();
    }

    /**
     * Gets capacity.
     *
     * @return the capacity
     */
    public Map<String, Integer> getCapacity() {
        return capacity;
    }

    /**
     * Gets capacity.
     *
     * @return the capacity
     */
    public int getResourceCapacity(String resourceName) {
        return capacity.getOrDefault(resourceName, -1);
    }
}
