package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Node extends GraphElement {
    private String name;
    private ActivationMode activationMode;

    private boolean enabled = true;

    private Set<ResourceConnection> incomingConnections = new HashSet<>();
    private Set<ResourceConnection> outgoingConnections = new HashSet<>();

    private FlowMode flowMode;
    protected ResourceSet resources = new ResourceSet();

    private Set<Modifier> modifiers = new HashSet<>();
    private Set<Trigger> triggers = new HashSet<>();
    private Set<Activator> activators = new HashSet<>();

    private boolean initialized = false;
    private IntegerExpression modifiedResourceSize = null;

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
    public Node setName(String name) {
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
    public Node setActivationMode(ActivationMode activationMode) {
        this.activationMode = activationMode;
        return this;
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets enabled.
     *
     * @param enabled the enabled
     * @return the enabled
     */
    public Node setEnabled(boolean enabled) {
        this.enabled = enabled;
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
    public Node setFlowMode(FlowMode flowMode) {
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

    /**
     * Gets requirements for firing this node.
     * @return the fire requirements
     */
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

    /**
     * Gets the resources stored in this node.
     * @return the resource set.
     */
    public ResourceSet getResources() {
        // By default, non-storing nodes do not provide any resources.
        return ResourceSet.empty();
    }

    /**
     * Extracts a certain amount of resource from the node.
     * @param resourceSet
     * @return
     */
    public ResourceSet extract(ResourceSet resourceSet) {
        return ResourceSet.empty();
    }

    /**
     * Receives a given amount of resources.
     * @param resourceSet
     * @return
     */
    public boolean receive(ResourceSet resourceSet) {
        return false;
    }

    /**
     * Activates the node.
     *
     * @return true if the node needs to be fired after activation, false otherwise.
     */
    public final boolean activate() {
        return doActivate();
    }

    /**
     * Do activate boolean.
     *
     * @return the boolean
     */
    protected boolean doActivate() {
        return true;
    }

    /**
     * Activates a node, giving incoming resource flow.
     * @param incomingResources incoming resources
     */
    public Set<ResourceConnection> fire(ResourceSet incomingResources) {
        // By default, does not fire any output resource connections.
        return Collections.emptySet();
    }

    /**
     * Activate triggers set.
     *
     * @return the set
     */
    public Set<Trigger> activateTriggers() {
        return this.triggers;
//        return this.triggers.stream()
//                .filter(t -> {
//                    Expression expression = t.getLabelExpression();
//
//                    if (expression instanceof LogicalExpression)
//                        return ((LogicalExpression)expression).eval();
//
//                    return true;
//                }).collect(Collectors.toSet());
    }

    /**
     * Evaluate int.
     *
     * @return the int
     */
    public int evaluate() {
        return evaluate(null);
    }

    /**
     * Evaluate int.
     *
     * @param context the context
     * @return the int
     */
    public int evaluate(NodeEvaluationContext context) {
        this.initializeIfNeeded();

        if (context != null && context.getRequester() instanceof Modifier) {
            return this.resources.deltaSize();
        }

        return evaluateResourceSize();
    }

    private int evaluateResourceSize() {
        if (this.modifiedResourceSize != null)
            return modifiedResourceSize.eval();
        else
            return this.resources.size();
    }

    /**
     *
     *
     */
    private void initializeIfNeeded() {
        if (initialized)
            return;

        this.initialized = true;

        if (!getModifiedBy().isEmpty()) {
            Set<Modifier> modifiedBy = this.getModifiedBy();

            Set<ValueModifier> valueModifiers = modifiedBy.stream()
                    .filter(m -> m instanceof ValueModifier).map(m -> (ValueModifier)m)
                    .collect(Collectors.toSet());

            Variable resourceSize = Variable.of("", () -> (float)this.resources.size());

            if (!valueModifiers.isEmpty()) {
                IntegerExpression modifiedValue = valueModifiers.stream()
                        .map(m -> {
                            IntegerExpression value = m.getValue();
                            NodeRef nodeRef = NodeRef.of(m.getOwner()).setContext(
                                    new NodeEvaluationContext().setRequester(m)
                            );
                            return (IntegerExpression) Multiplication.of(nodeRef, value);
                        })
                        .reduce(resourceSize, Addition::of);

                this.modifiedResourceSize = modifiedValue;
            }
        }
    }
}
