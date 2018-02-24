package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.*;

public class Trigger extends Element {
    private static final LogicalExpression DEFAULT_CONDITION = BooleanValue.of(true);
    private static final Percentage DEFAULT_PROBABILITY = Percentage.of(100);
    private static final IntegerExpression DEFAULT_DISTRIBUTION = FixedInteger.of(1.0f);

    private Node owner;
    private GraphElement target;

    private LogicalExpression condition = DEFAULT_CONDITION;
    private Percentage probability = DEFAULT_PROBABILITY;
    private IntegerExpression distribution = DEFAULT_DISTRIBUTION;

    private boolean usingProbability = false;

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public Node getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     * @return the owner
     */
    public Trigger setOwner(Node owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public GraphElement getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target the target
     * @return the target
     */
    public Trigger setTarget(GraphElement target) {
        this.target = target;
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
    public Trigger setCondition(LogicalExpression condition) {
        this.condition = condition;
        return this;
    }

    /**
     * Gets probability.
     *
     * @return the probability
     */
    public Percentage getProbability() {
        return probability;
    }

    /**
     * Sets probability.
     *
     * @param probability the probability
     * @return the probability
     */
    public Trigger setProbability(Percentage probability) {
        this.probability = probability;
        return this;
    }

    /**
     * Gets distribution.
     *
     * @return the distribution
     */
    public IntegerExpression getDistribution() {
        return distribution;
    }

    /**
     * Sets distribution.
     *
     * @param distribution the distribution
     * @return the distribution
     */
    public Trigger setDistribution(IntegerExpression distribution) {
        this.distribution = distribution;
        return this;
    }

    /**
     * Is using probability boolean.
     *
     * @return the boolean
     */
    public boolean isUsingProbability() {
        return usingProbability;
    }

    /**
     * Sets using probability.
     *
     * @param usingProbability the using probability
     * @return the using probability
     */
    public Trigger setUsingProbability(boolean usingProbability) {
        this.usingProbability = usingProbability;
        return this;
    }
}
