package com.squarebit.machinations.models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractNode extends AbstractElement {
    private String name;
    private ActivationMode activationMode;
    private FlowMode flowMode;
    private Set<AbstractConnection> incomingConnections = new HashSet<>();
    private Set<AbstractConnection> outgoingConnections = new HashSet<>();
    protected ResourceContainer resources = new ResourceContainer();

    private Set<Modifier> modifiers = new HashSet<>();
    private Set<Trigger> triggers = new HashSet<>();
    private Set<Activator> activators = new HashSet<>();

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
     *
     * @return
     */
    public boolean isPulling() {
        return (
                flowMode == FlowMode.PULL_ALL || flowMode == FlowMode.PULL_ANY ||
                        (flowMode == FlowMode.AUTOMATIC && outgoingConnections.size() == 0)
        );
    }

    public boolean isAllOrNoneFlow() {
        return (flowMode == FlowMode.PULL_ALL || flowMode == FlowMode.PUSH_ALL);
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
        return resources.content;
    }

    /**
     * Gets resource count by name.
     *
     * @param name the name
     * @return the resource
     */
    public int getResourceCount(String name) {
        return resources.get(name);
    }

    /**
     *
     * @return
     */
    public int getTotalResourceCount() {
        return resources.size();
    }

    /**
     * Gets capacity.
     *
     * @return the capacity
     */
    public Map<String, Integer> getCapacity() {
        return resources.capacity;
    }

    /**
     * Gets capacity.
     *
     * @return the capacity
     */
    public int getResourceCapacity(String resourceName) {
        return resources.getCapacity(resourceName);
    }

    /**
     *
     * @param amount
     */
    public void removeResource(ResourceContainer amount) {
        this.resources.remove(amount);
    }

    /**
     *
     * @param amount
     */
    public void addResource(ResourceContainer amount) {
        this.resources.add(amount);
    }

    /**
     * Gets modifiers.
     *
     * @return the modifiers
     */
    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    /**
     * Gets triggers.
     *
     * @return the triggers
     */
    public Set<Trigger> getTriggers() {
        return triggers;
    }

    /**
     * Gets activators.
     *
     * @return the activators
     */
    public Set<Activator> getActivators() {
        return activators;
    }

    @Override
    protected void doActivate(int time) {
        super.doActivate(time);
    }
}
