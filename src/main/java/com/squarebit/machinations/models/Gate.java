package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.*;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Gate extends Node {
    private static final ResourceConnection NULL_CONNECTION = new ResourceConnection();
    private static final Trigger NULL_TRIGGER = new Trigger();
    private static final DiceNumber DEFAULT_DRAW = new DiceNumber();

    private boolean random = false;
    private ArithmeticExpression drawExpression = DEFAULT_DRAW;

    private boolean isInitialized = false;
    private boolean useProbableOutputs = true;
    private boolean useProbableTriggers = true;

    private EnumeratedDistribution<ResourceConnection> outgoingProbabilities;
    private int passedThroughResources = 0;
    private int currentDraw = 0;

    private EnumeratedDistribution<Trigger> triggerProbabilities;

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

    /**
     * Gets draw expression.
     *
     * @return the draw expression
     */
    public ArithmeticExpression getDrawExpression() {
        return drawExpression;
    }

    /**
     * Sets draw expression.
     *
     * @param drawExpression the draw expression
     * @return the draw expression
     */
    public Gate setDrawExpression(ArithmeticExpression drawExpression) {
        this.drawExpression = drawExpression;
        return this;
    }

    @Override
    public int evaluate(NodeEvaluationContext context) {
        if (context.getOwner() instanceof ResourceConnection) {
            if (!this.random)
                return this.passedThroughResources;
            else
                return this.currentDraw;
        }
        else if (context.getOwner() instanceof Trigger) {
            return this.currentDraw;
        }
        else
            throw new RuntimeException("Should not reach here.");
    }

    @Override
    public Set<ResourceConnection> __activate(int time, Map<ResourceConnection, ResourceSet> incomingFlows) {
        this.initializeIfNeeded();

        Set<ResourceConnection> outgoingConnections = new HashSet<>();

        // Total incoming resources.
        ResourceSet resources = new ResourceSet();
        incomingFlows.forEach((c, a) -> {
            c.getFrom().extract(a);
            resources.add(a);
        });

        if (this.isRandom()) {
            if (this.useProbableOutputs) {
                ResourceConnection connection = this.outgoingProbabilities.sample();
                if (connection != NULL_CONNECTION) {
                    outgoingConnections.add(connection);
                    connection.getTo().receive(resources);
                }
            }
            else {
                currentDraw = drawExpression.eval();
                this.getOutgoingConnections().forEach(c -> {
                    LogicalExpression expression = (LogicalExpression)c.getFlowRateExpression();
                    if (expression.eval()) {
                        outgoingConnections.add(c);
                        c.getTo().receive(resources);
                    }
                });
            }
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
                        LogicalExpression expression = (LogicalExpression)c.getFlowRateExpression();
                        if (expression.eval()) {
                            outgoingConnections.add(c);
                            c.getTo().receive(extracted);
                        }
                    });

                    this.passedThroughResources++;
                }
            }
        }

        return outgoingConnections;
    }

    @Override
    public Set<Trigger> __activateTriggers() {
        Set<Trigger> triggers = new HashSet<>();

//        if (this.useProbableTriggers) {
//            Trigger trigger = this.triggerProbabilities.sample();
//            if (trigger != NULL_TRIGGER) {
//                triggers.add(trigger);
//            }
//        }
//        else {
//            currentDraw = drawExpression.eval();
//            this.getTriggers().forEach(t -> {
//                LogicalExpression expression = (LogicalExpression)t.getLabelExpression();
//                if (expression.eval()) {
//                    triggers.add(t);
//                }
//            });
//        }

        return triggers;
    }

    /**
     * Initializes the node if needed.
     */
    private void initializeIfNeeded() {
        if (this.isInitialized)
            return;

        isInitialized = true;

        currentDraw = drawExpression.eval();

        {
            Set<ResourceConnection> connections = this.getOutgoingConnections();
            boolean allArithmetic = connections.stream()
                    .allMatch(c -> c.getFlowRateExpression() instanceof ArithmeticExpression);

            boolean allConditional = connections.stream()
                    .allMatch(c -> c.getFlowRateExpression() instanceof LogicalExpression);

            if (!allArithmetic && !allConditional)
                throw new RuntimeException("Gate outgoing connections must agree on their expression types.");

            this.useProbableOutputs = allArithmetic;

            if (this.useProbableOutputs) {
                Map<ResourceConnection, Float> probabilities = new HashMap<>();
//                connections.forEach(c ->
//                        probabilities.put(c, ((ArithmeticExpression)c.getFlowRateExpression()).nonZeroProbability())
//                );

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

        {
            Set<Trigger> triggers = this.getTriggers();
//            boolean allArithmetic = triggers.stream()
//                    .allMatch(c -> c.getLabelExpression() instanceof ArithmeticExpression);
//
//            boolean allConditional = triggers.stream()
//                    .allMatch(c -> c.getLabelExpression() instanceof LogicalExpression);
//
//            if (!allArithmetic && !allConditional)
//                throw new RuntimeException("Gate triggers must agree on their expression types.");
//
//            this.useProbableTriggers = allArithmetic;
//
//            if (this.useProbableTriggers) {
//                Map<Trigger, Float> probabilities = new HashMap<>();
////                getTriggers().forEach(t ->
////                        probabilities.put(t, ((ArithmeticExpression)t.getLabelExpression()).nonZeroProbability())
////                );
//
//                float sumProb = (float)probabilities.values().stream().mapToDouble(v -> v).sum();
//                if (sumProb < 1.0f) {
//                    probabilities.put(NULL_TRIGGER, 1.0f - sumProb);
//                }
//
//                this.triggerProbabilities = new EnumeratedDistribution<>(
//                        probabilities.entrySet().stream()
//                                .map(e -> new Pair<>(e.getKey(), (double)e.getValue()))
//                                .collect(Collectors.toList())
//                );
//            }
        }
    }
}
