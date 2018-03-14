package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.FixedInteger;
import com.squarebit.machinations.engine.IntegerExpression;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The queue node.
 */
public class Queue extends Node {
    private static final IntegerExpression DEFAULT_DELAY = FixedInteger.of(1);
    private IntegerExpression delay = DEFAULT_DELAY;

    /**
     * Gets delay.
     *
     * @return the delay
     */
    public IntegerExpression getDelay() {
        return delay;
    }

    /**
     * Sets delay.
     *
     * @param delay the delay
     * @return the delay
     */
    public Queue setDelay(IntegerExpression delay) {
        this.delay = delay;
        return this;
    }

    /**
     * Activates a node, giving incoming resource flow.
     *
     * @param incomingResources incoming resources
     */
    @Override
    public Set<ResourceConnection> fire(ResourceSet incomingResources) {
        int delaySteps = this.delay.eval();
        if (delaySteps > 0) {
            this.resources.add(incomingResources);
            machinations.registerLateFiringAction(delaySteps, () -> dispatch(incomingResources));
            return Collections.emptySet();
        }
        else
            return dispatch(incomingResources);
    }

    /**
     *
     * @param incomingResources
     * @return
     */
    private Set<ResourceConnection> dispatch(ResourceSet incomingResources) {
        Set<ResourceConnection> connections = new HashSet<>();

        this.getOutgoingConnections().forEach(c -> {
            ResourceSet requiredResources = c.fire();
            ResourceSet extracted = this.resources.remove(requiredResources);

            if (extracted.size() > 0) {
                c.getTo().receive(extracted);
                connections.add(c);
            }
        });

        return connections;
    }
}
