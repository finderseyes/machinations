package com.squarebit.machinations.models;

import java.util.Set;

public class FireRequirement {
    private Node target;
    private Set<ResourceConnection> connections;
    private boolean requiringAll = false;

    private FireRequirement() {

    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public Node getTarget() {
        return target;
    }

    private FireRequirement setTarget(Node target) {
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

    private FireRequirement setConnections(Set<ResourceConnection> connections) {
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

    private FireRequirement setRequiringAll(boolean requiringAll) {
        this.requiringAll = requiringAll;
        return this;
    }

    /**
     * All activation requirement.
     *
     * @param connections the connections
     * @return the activation requirement
     */
    public static FireRequirement all(Node target, Set<ResourceConnection> connections) {
        return new FireRequirement()
                .setRequiringAll(true).setConnections(connections).setTarget(target);
    }

    /**
     * Any activation requirement.
     *
     * @param connections the connections
     * @return the activation requirement
     */
    public static FireRequirement any(Node target, Set<ResourceConnection> connections) {
        return new FireRequirement()
                .setRequiringAll(false).setConnections(connections).setTarget(target);
    }
}
