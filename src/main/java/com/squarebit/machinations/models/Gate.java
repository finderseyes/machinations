package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.ArithmeticExpression;
import com.squarebit.machinations.engine.BooleanExpression;
import com.squarebit.machinations.engine.Expression;
import com.squarebit.machinations.engine.ExpressionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Gate extends AbstractNode {

    private boolean random = false;
    private boolean isInitialized = false;
    private boolean useProbableOutputs = true;

    private Map<ResourceConnection, Float> probabilities = new HashMap<>();

    /**
     * Is random gate?.
     *
     * @return the boolean
     */
    public boolean isRandom() {
        return random;
    }

    /**
     * Sets random.
     *
     * @param random the random
     * @return the random
     */
    public Gate setRandom(boolean random) {
        this.random = random;
        return this;
    }

    @Override
    public Set<ResourceConnection> activate(int time, Map<ResourceConnection, ResourceSet> incomingFlows) {
        this.initializeIfNeeded();

        // Total incoming resources.
        ResourceSet resources = incomingFlows.values().stream()
                .reduce(new ResourceSet(), (a, b) -> {
                    a.add(b);
                    return a;
                });

        if (this.isRandom()) {
            return super.activate(time, incomingFlows);
        }
        else {
            // Deterministic. Distribute incoming resources one by one.
            while (resources.size() > 0) {

            }
            return super.activate(time, incomingFlows);
        }
    }

    /**
     * Initializes the node if needed.
     */
    private void initializeIfNeeded() {
        if (this.isInitialized)
            return;

        isInitialized = true;

        Set<ResourceConnection> connections = this.getOutgoingConnections();
        boolean allArithmetic = connections.stream()
                .allMatch(c -> c.getFlowRateExpression() instanceof ArithmeticExpression);

        boolean allConditional = connections.stream()
                .allMatch(c -> c.getFlowRateExpression() instanceof BooleanExpression);

        if (!allArithmetic && !allConditional)
            throw new RuntimeException("Gate outgoing connections must agree on their expression types.");

        this.useProbableOutputs = allArithmetic;

        if (this.useProbableOutputs) {
            connections.forEach(c ->
                    probabilities.put(c, ((ArithmeticExpression)c.getFlowRateExpression()).evaluateAsProbable())
            );
        }
    }
}
