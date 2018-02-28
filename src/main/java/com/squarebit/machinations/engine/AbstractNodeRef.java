package com.squarebit.machinations.engine;

import com.squarebit.machinations.models.Node;
import com.squarebit.machinations.models.NodeEvaluationContext;

public class AbstractNodeRef extends ArithmeticExpression {
    private Node node;
    private NodeEvaluationContext context;

    /**
     * Gets node.
     *
     * @return the node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Sets node.
     *
     * @param node the node
     * @return the node
     */
    public AbstractNodeRef setNode(Node node) {
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

    public static AbstractNodeRef of(Node node) {
        return new AbstractNodeRef().setNode(node);
    }
}
