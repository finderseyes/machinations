package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.ArithmeticExpression;
import com.squarebit.machinations.engine.BooleanExpression;
import com.squarebit.machinations.engine.Expression;
import com.squarebit.machinations.engine.ExpressionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Gate extends AbstractNode {
    private static final ResourceConnection NULL_CONNECTION = new ResourceConnection();

    private boolean random = false;
    private boolean isInitialized = false;
    private boolean useProbableOutputs = true;

    private EnumeratedDistribution<ResourceConnection> outgoingProbabilities;
    private int passedThroughResources = 0;

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
    public int evaluate() {
        if (!this.random)
            return this.passedThroughResources;
        else
            return RandomUtils.nextInt(1, 6);
    }

    @Override
    public Set<ResourceConnection> activate(int time, Map<ResourceConnection, ResourceSet> incomingFlows) {
        this.initializeIfNeeded();

        Set<ResourceConnection> outgoingConnections = new HashSet<>();

        // Total incoming resources.
        ResourceSet resources = new ResourceSet();
        incomingFlows.forEach((c, a) -> {
            c.getFrom().extract(a);
            resources.add(a);
        });

        if (this.isRandom()) {
            return super.activate(time, incomingFlows);
        }
        else {
            if (this.useProbableOutputs) {
                // Deterministic. Distribute incoming resources one by one.
                while (resources.size() > 0) {
                    ResourceSet extracted = resources.remove(1);
                    this.passedThroughResources++;

                    ResourceConnection connection = this.outgoingProbabilities.sample();
                    if (connection != NULL_CONNECTION) {
                        outgoingConnections.add(connection);
                        connection.getTo().receive(extracted);
                    }
                }
            }
            else {
                while (resources.size() > 0) {
                    ResourceSet extracted = resources.remove(1);

                    this.getOutgoingConnections().forEach(c -> {
                        BooleanExpression expression = (BooleanExpression)c.getFlowRateExpression();
                        if (expression.evaluate()) {
                            outgoingConnections.add(c);
                            c.getTo().receive(extracted);
                        }
                    });

                    this.passedThroughResources++;
                }
            }

            return outgoingConnections;
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
            Map<ResourceConnection, Float> probabilities = new HashMap<>();
            connections.forEach(c ->
                    probabilities.put(c, ((ArithmeticExpression)c.getFlowRateExpression()).evaluateAsProbable())
            );

            float sumProb = (float)probabilities.values().stream().mapToDouble(v -> v).sum();
            if (sumProb < 1.0f) {
                probabilities.put(NULL_CONNECTION, 1.0f - sumProb);
            }

            List<Pair<ResourceConnection, Double>> items = probabilities.entrySet().stream()
                    .map(e -> new Pair<>(e.getKey(), (double)e.getValue()))
                    .collect(Collectors.toList());
            this.outgoingProbabilities = new EnumeratedDistribution<>(items);
        }
    }
}
