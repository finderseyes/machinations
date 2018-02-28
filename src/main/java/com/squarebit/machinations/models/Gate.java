package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.*;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Gate extends Node {
    private static final ResourceConnection NULL_CONNECTION = new ResourceConnection();
    private static final Trigger NULL_TRIGGER = new Trigger();
    private static final IntegerExpression DEFAULT_DRAW = RandomInteger.of(1, 6);

    private boolean random = false;
    private IntegerExpression drawExpression = DEFAULT_DRAW;

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
    public IntegerExpression getDrawExpression() {
        return drawExpression;
    }

    /**
     * Sets draw expression.
     *
     * @param drawExpression the draw expression
     * @return the draw expression
     */
    public Gate setDrawExpression(IntegerExpression drawExpression) {
        this.drawExpression = drawExpression;
        return this;
    }

    @Override
    public int evaluate(NodeEvaluationContext context) {
        if (context.getRequester() instanceof ResourceConnection) {
            if (!this.random)
                return this.passedThroughResources;
            else
                return this.currentDraw;
        }
        else if (context.getRequester() instanceof Trigger) {
            return this.currentDraw;
        }
        else
            return super.evaluate(context);
    }

    @Override
    public Set<ResourceConnection> fire(ResourceSet incomingFlows) {
        this.initializeIfNeeded();

        Set<ResourceConnection> outgoingConnections = new HashSet<>();

        if (this.isRandom()) {
            if (this.useProbableOutputs) {
                ResourceConnection connection = this.outgoingProbabilities.sample();
                if (connection != NULL_CONNECTION) {
                    outgoingConnections.add(connection);
                    connection.getTo().receive(incomingFlows);
                }
            }
            else {
                currentDraw = drawExpression.eval();
                this.getOutgoingConnections().forEach(c -> {
                    LogicalExpression expression = c.getCondition();
                    if (expression.eval()) {
                        outgoingConnections.add(c);
                        c.getTo().receive(incomingFlows);
                    }
                });
            }
        }
        else {
            if (this.useProbableOutputs) {
                // Deterministic. Distribute incoming resources one by one.
                while (incomingFlows.size() > 0) {
                    ResourceSet extracted = incomingFlows.remove(1);
                    this.passedThroughResources++;

                    ResourceConnection connection = this.outgoingProbabilities.sample();
                    if (connection != NULL_CONNECTION) {
                        outgoingConnections.add(connection);
                        connection.getTo().receive(extracted);
                    }
                }
            }
            else {
                while (incomingFlows.size() > 0) {
                    ResourceSet extracted = incomingFlows.remove(1);

                    this.getOutgoingConnections().forEach(c -> {
                        LogicalExpression expression = c.getCondition();
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
    public Set<Trigger> activateTriggers() {
        Set<Trigger> triggers = new HashSet<>();

        if (this.useProbableTriggers) {
            Trigger trigger = this.triggerProbabilities.sample();
            if (trigger != NULL_TRIGGER) {
                triggers.add(trigger);
            }
        }
        else {
            currentDraw = drawExpression.eval();
            this.getTriggers().forEach(t -> {
                LogicalExpression expression = t.getCondition();
                if (expression.eval()) {
                    triggers.add(t);
                }
            });
        }

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
            boolean allProbableOutputs = connections.stream()
                    .allMatch(c -> c.getCondition() == ResourceConnection.DEFAULT_CONDITION);

            boolean allConditional = connections.stream()
                    .allMatch(c -> c.getCondition() != ResourceConnection.DEFAULT_CONDITION);

            if (!allProbableOutputs && !allConditional)
                throw new RuntimeException("Gate output connections must agree on their expression types.");

            this.useProbableOutputs = allProbableOutputs;

            if (allProbableOutputs) {
                Map<ResourceConnection, Float> probabilities = new HashMap<>();
                boolean useFlowProbability = connections.stream()
                        .allMatch(c -> c.getFlowRate().getProbability() != FlowRate.DEFAULT_PROBABILITY);

                if (useFlowProbability) {
                    float sum = (float)connections.stream()
                            .mapToDouble(c -> c.getFlowRate().getProbability().eval() * 1e-2f).sum();
                    connections.forEach(c -> {
                        FlowRate flowRate = c.getFlowRate();
                        probabilities.put(c, flowRate.getProbability().eval() * 1e-2f);
                    });

                    if (sum < 1.0f) {
                        probabilities.put(NULL_CONNECTION, 1.0f - sum);
                    }
                }
                else {
                    float sum = (float)connections.stream().mapToDouble(c -> c.getFlowRate().getValue().eval()).sum();
                    connections.forEach(c -> {
                        FlowRate flowRate = c.getFlowRate();
                        probabilities.put(c, (flowRate.getValue().eval()/sum));
                    });
                }

                List<Pair<ResourceConnection, Double>> items = probabilities.entrySet().stream()
                        .map(e -> new Pair<>(e.getKey(), (double)e.getValue()))
                        .collect(Collectors.toList());

                this.outgoingProbabilities = new EnumeratedDistribution<>(items);
            }
        }

        {
            Set<Trigger> triggers = this.getTriggers();
            boolean allProbability = triggers.stream().allMatch(Trigger::isUsingProbability);

            boolean allConditional = triggers.stream().noneMatch(Trigger::isUsingProbability);

            if (!allProbability && !allConditional)
                throw new RuntimeException("Gate triggers must agree on their expression types.");

            this.useProbableTriggers = allProbability;

            if (this.useProbableTriggers) {
                Map<Trigger, Float> probabilities = new HashMap<>();
                boolean useTriggerProbability = triggers.stream()
                        .allMatch(t -> t.getProbability() != Trigger.DEFAULT_PROBABILITY);

                if (useTriggerProbability) {
                    float sum = (float)triggers.stream()
                            .mapToDouble(t -> t.getProbability().eval() * 1e-2f).sum();
                    triggers.forEach(t -> probabilities.put(t, t.getProbability().eval() * 1e-2f));

                    if (sum < 1.0f) {
                        probabilities.put(NULL_TRIGGER, 1.0f - sum);
                    }
                }
                else {
                    float sum = (float)triggers.stream().mapToDouble(c -> c.getDistribution().eval()).sum();
                    triggers.forEach(t -> probabilities.put(t, (float)t.getDistribution().eval()/sum));
                }

                List<Pair<Trigger, Double>> items = probabilities.entrySet().stream()
                        .map(e -> new Pair<>(e.getKey(), (double)e.getValue()))
                        .collect(Collectors.toList());

                this.triggerProbabilities = new EnumeratedDistribution<>(items);
            }
        }
    }
}
