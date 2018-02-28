package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.*;

import java.util.Set;
import java.util.stream.Collectors;

public class ResourceConnection extends Connection {
    public static final LogicalExpression DEFAULT_CONDITION = BooleanValue.of(true);

    private Node from;
    private Node to;

    private LogicalExpression condition = DEFAULT_CONDITION;
    private FlowRate flowRate = new FlowRate();
    private String resourceName = null; // if null, any resource.

    private boolean initialized = false;
    private FlowRate modifiedFlowRate = null;

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
        this.initializeIfNeeded();

        if (condition.eval()) {
            int amount = modifiedFlowRate != null ? modifiedFlowRate.get() : flowRate.get();
            amount = Math.max(amount, 0);
            return ResourceSet.of(this.resourceName, amount);
        }
        else
            return ResourceSet.empty();
    }

    /**
     * Initializes the connection if needed.
     */
    private void initializeIfNeeded() {
        if (this.initialized)
            return;

        this.initialized = true;

        if (!getModifiedBy().isEmpty()) {
            FlowRate modifiedFlowRate = new FlowRate();
            modifiedFlowRate
                    .setValue(this.flowRate.getValue())
                    .setInterval(this.flowRate.getInterval())
                    .setMultiplier(this.flowRate.getMultiplier())
                    .setProbability(this.flowRate.getProbability());

            Set<Modifier> modifiedBy = this.getModifiedBy();

            // Value modifiers
            {
                Set<ValueModifier> valueModifiers = modifiedBy.stream()
                        .filter(m -> m instanceof ValueModifier).map(m -> (ValueModifier)m)
                        .collect(Collectors.toSet());

                if (!valueModifiers.isEmpty()) {
                    IntegerExpression modifiedValue = valueModifiers.stream()
                            .map(m -> {
                                IntegerExpression value = m.getValue();
                                NodeRef nodeRef = NodeRef.of(m.getOwner()).setContext(
                                        new NodeEvaluationContext().setRequester(m)
                                );
                                return (IntegerExpression)Multiplication.of(nodeRef, value);
                            })
                            .reduce(this.flowRate.getValue(), Addition::of);

                    modifiedFlowRate.setValue(modifiedValue);
                }
            }

            // Interval modifiers
            {
                Set<IntervalModifier> intervalModifiers = modifiedBy.stream()
                        .filter(m -> m instanceof IntervalModifier).map(m -> (IntervalModifier)m)
                        .collect(Collectors.toSet());

                if (!intervalModifiers.isEmpty()) {
                    IntegerExpression modifiedValue = intervalModifiers.stream()
                            .map(m -> {
                                IntegerExpression value = m.getValue();
                                NodeRef nodeRef = NodeRef.of(m.getOwner()).setContext(
                                        new NodeEvaluationContext().setRequester(m)
                                );
                                return (IntegerExpression)Multiplication.of(nodeRef, value);
                            })
                            .reduce(this.flowRate.getInterval(), Addition::of);

                    modifiedFlowRate.setInterval(modifiedValue);
                }
            }

            // Multipliers
            {
                Set<MultiplierModifier> multiplierModifiers = modifiedBy.stream()
                        .filter(m -> m instanceof MultiplierModifier).map(m -> (MultiplierModifier)m)
                        .collect(Collectors.toSet());

                if (!multiplierModifiers.isEmpty()) {
                    IntegerExpression modifiedValue = multiplierModifiers.stream()
                            .map(m -> {
                                IntegerExpression value = m.getValue();
                                NodeRef nodeRef = NodeRef.of(m.getOwner()).setContext(
                                        new NodeEvaluationContext().setRequester(m)
                                );
                                return (IntegerExpression)Multiplication.of(nodeRef, value);
                            })
                            .reduce(this.flowRate.getMultiplier(), Addition::of);

                    modifiedFlowRate.setMultiplier(modifiedValue);
                }
            }

            this.modifiedFlowRate = modifiedFlowRate;
        }
    }
}
