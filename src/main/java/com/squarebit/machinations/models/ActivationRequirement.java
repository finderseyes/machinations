package com.squarebit.machinations.models;

import java.util.Set;

public class ActivationRequirement {
    private AbstractNode target;
    private Set<ResourceConnection> connections;
    private boolean requiringAll = false;

    private ActivationRequirement() {

    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public AbstractNode getTarget() {
        return target;
    }

    private ActivationRequirement setTarget(AbstractNode target) {
        this.target = target;
        return this;
    }

    /**
     * Gets resource connections.
     *
     * @return the resource connections
     */
    public Set<ResourceConnection> getConnections() {
        return connections;
    }

    private ActivationRequirement setConnections(Set<ResourceConnection> connections) {
        this.connections = connections;
        return this;
    }

    /**
     * Is requiring all boolean.
     *
     * @return the boolean
     */
    public boolean isRequiringAll() {
        return requiringAll;
    }

    private ActivationRequirement setRequiringAll(boolean requiringAll) {
        this.requiringAll = requiringAll;
        return this;
    }

    /**
     * All activation requirement.
     *
     * @param connections the connections
     * @return the activation requirement
     */
    public static ActivationRequirement all(AbstractNode target, Set<ResourceConnection> connections) {
        return new ActivationRequirement()
                .setRequiringAll(true).setConnections(connections).setTarget(target);
    }

    /**
     * Any activation requirement.
     *
     * @param connections the connections
     * @return the activation requirement
     */
    public static ActivationRequirement any(AbstractNode target, Set<ResourceConnection> connections) {
        return new ActivationRequirement()
                .setRequiringAll(false).setConnections(connections).setTarget(target);
    }
}