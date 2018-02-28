package com.squarebit.machinations.engine;

import com.squarebit.machinations.models.Node;
import com.squarebit.machinations.models.NodeEvaluationContext;

public class NodeRef extends IntegerExpression {
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
    public NodeRef setNode(Node node) {
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
    public NodeRef setContext(NodeEvaluationContext context) {
        this.context = context;
        return this;
    }

    /**
     * Evaluates the expression to universal numerical type (float).
     *
     * @return value as float
     */
    @Override
    public float evalAsFloat() {
        return node.evaluate(context);
    }

    public static NodeRef of(Node node) {
        return new NodeRef().setNode(node);
    }

    /**
     * Determines if the expression evaluates to a random value.
     *
     * @return true if the expression value is random, false otherwise
     */
    @Override
    public boolean isRandom() {
        return false;
    }
}
