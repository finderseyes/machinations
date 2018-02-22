package com.squarebit.machinations.engine;

import com.squarebit.machinations.models.AbstractNode;
import com.squarebit.machinations.models.NodeEvaluationContext;

public class AbstractNodeRef extends ArithmeticExpression {
    private AbstractNode node;
    private NodeEvaluationContext context;

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
     * Gets context.
     *
     * @return the context
     */
    public NodeEvaluationContext getContext() {
        return context;
    }

    /**
     * Sets context.
     *
     * @param context the context
     * @return the context
     */
    public AbstractNodeRef setContext(NodeEvaluationContext context) {
        this.context = context;
        return this;
    }

    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int eval() {
        // Default to getting all resources count.
        return node.evaluate(context);
    }

    public static AbstractNodeRef of(AbstractNode node) {
        return new AbstractNodeRef().setNode(node);
    }
}
