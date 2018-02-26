package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.*;

public class ResourceConnection extends Connection {
    private static final LogicalExpression DEFAULT_CONDITION = BooleanValue.of(true);

    private Node from;
    private Node to;

    private LogicalExpression condition = DEFAULT_CONDITION;
    private FlowRate flowRate = new FlowRate();
    private String resourceName = null; // if null, any resource.

    /**
     * Gets from.
     *
     * @return the from
     */
    public Node getFrom() {
        return from;
    }

    /**
     * Sets from.
     *
     * @param from the from
     * @return the from
     */
    public ResourceConnection setFrom(Node from) {
        this.from = from;
        return this;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public Node getTo() {
        return to;
    }

    /**
     * Sets to.
     *
     * @param to the to
     * @return the to
     */
    public ResourceConnection setTo(Node to) {
        this.to = to;
        return this;
    }

    /**
     * Gets condition.
     *
     * @return the condition
     */
    public LogicalExpression getCondition() {
        return condition;
    }

    /**
     * Sets condition.
     *
     * @param condition the condition
     * @return the condition
     */
    public ResourceConnection setCondition(LogicalExpression condition) {
        this.condition = condition;
        return this;
    }

    /**
     * Gets flow rate.
     *
     * @return the flow rate
     */
    public FlowRate getFlowRate() {
        return flowRate;
    }

    /**
     * Sets flow rate.
     *
     * @param flowRate the flow rate
     * @return the flow rate
     */
    public ResourceConnection setFlowRate(FlowRate flowRate) {
        this.flowRate = flowRate;
        return this;
    }

    /**
     * Gets resource name.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Sets resource name.
     *
     * @param resourceName the resource name
     * @return the resource name
     */
    public ResourceConnection setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    /**
     * Activates the resource connection and gets the required resource set should be passed on it.
     *
     * @return the required resource set
     */
    public ResourceSet fire() {
        if (condition.eval()) {
            int amount = flowRate.get();
            return ResourceSet.of(this.resourceName, amount);
        }
        else
            return ResourceSet.empty();
    }
}
