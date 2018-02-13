package com.squarebit.machinations.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractNode extends AbstractElement {
    private String name;
    private ActivationMode activationMode;

    private Set<ResourceConnection> incomingConnections = new HashSet<>();
    private Set<ResourceConnection> outgoingConnections = new HashSet<>();

    private FlowMode flowMode;
    protected ResourceSet resources = new ResourceSet();

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
                        (flowMode == FlowMode.AUTOMATIC &&
                                (outgoingConnections.size() == 0 || incomingConnections.size() > 0))
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
    public Set<ResourceConnection> getIncomingConnections() {
        return incomingConnections;
    }

    /**
     * Gets outgoing connections.
     *
     * @return the outgoing connections
     */
    public Set<ResourceConnection> getOutgoingConnections() {
        return outgoingConnections;
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
    public void removeResource(ResourceSet amount) {
        this.resources.remove(amount);
    }

    /**
     *
     * @param amount
     */
    public void addResource(ResourceSet amount) {
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

    /**
     * Gets the activation requirement.
     * @return the activation requirement
     */
    public ActivationRequirement getActivationRequirement() {
        // By default, node is activated if any of its incoming resource connections is activated.
        return ActivationRequirement.any(this, incomingConnections);
    }

    /**
     * Gets the resources stored in this node.
     * @return the resource set.
     */
    public ResourceSet getResources() {
        // By default, non-storing nodes do not provide any resources.
        return ResourceSet.EMPTY_SET;
    }

    /**
     * Determines if the node can provide a set of resources, without providing exact number of sub resources.
     * @param resourceSet the resource set.
     * @return true or false
     */
    public boolean canProvide(ResourceSet resourceSet) {
        // By default, non-storing node cannot provide any resources.
        return resourceSet.size() <= this.getResources().size();
    }

    /**
     * Determines if the node can provide exactly the given resource set.
     * @param resourceSet the resource set.
     * @return
     */
    public boolean canProvideExact(ResourceSet resourceSet) {
        // By default, non-storing node cannot provide any resources.
        return resourceSet.isSubSetOf(this.getResources());
    }

    public ResourceSet extract(ResourceSet resourceSet) {
        return ResourceSet.EMPTY_SET;
    }

    public ResourceSet extractExact(ResourceSet resourceSet) {
        return ResourceSet.EMPTY_SET;
    }

    public boolean receive(ResourceSet resourceSet) {
        return false;
    }

    /**
     * Activates a node, giving incoming resource flow.
     * @param time
     * @param incomingFlows
     */
    public Set<ResourceConnection> activate(int time, Map<ResourceConnection, ResourceSet> incomingFlows) {
        // By default, does not activate any output resource connections.
        return Collections.emptySet();
    }

    public Set<Trigger> activateTriggers() {
        return this.triggers;
    }

    public Set<Activator> activateActivators() {
        return this.activators;
    }
}
