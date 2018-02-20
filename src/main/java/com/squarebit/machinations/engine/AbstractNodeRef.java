package com.squarebit.machinations.engine;

import com.squarebit.machinations.models.AbstractNode;

public class AbstractNodeRef extends ArithmeticExpression {
    private AbstractNode node;

    /**
     * Gets node.
     *
     * @return the node
     */
    public AbstractNode getNode() {
        return node;
    }

    /**
     * Sets node.
     *
     * @param node the node
     * @return the node
     */
    public AbstractNodeRef setNode(AbstractNode node) {
        this.node = node;
        return this;
    }

    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int evaluate() {
        // Default to getting all resources count.
        return node.evaluate();
    }

    /**
     * Evaluate as probable and return probability.
     *
     * @return probability
     */
    @Override
    public float evaluateAsProbable() {
        int size = node.evaluate();

        if (size >= 1)
            return 1.0f;
        else
            return 0.0f;
    }

    public static AbstractNodeRef of(AbstractNode node) {
        return new AbstractNodeRef().setNode(node);
    }
}
