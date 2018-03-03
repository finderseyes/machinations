package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.GNode;

/**
 * The node transformation context.
 */
final class GNodeTransformationContext {
    private GGraphTransformationContext graphTransformationContext;
    private GNode.Modifier modifier;
    private GNode node;

    /**
     * Gets graph transformation context.
     *
     * @return the graph transformation context
     */
    public GGraphTransformationContext getGraphTransformationContext() {
        return graphTransformationContext;
    }

    /**
     * Sets graph transformation context.
     *
     * @param graphTransformationContext the graph transformation context
     * @return the graph transformation context
     */
    public GNodeTransformationContext setGraphTransformationContext(GGraphTransformationContext graphTransformationContext) {
        this.graphTransformationContext = graphTransformationContext;
        return this;
    }

    /**
     * Gets modifier.
     *
     * @return the modifier
     */
    public GNode.Modifier getModifier() {
        return modifier;
    }

    /**
     * Sets modifier.
     *
     * @param modifier the modifier
     * @return the modifier
     */
    public GNodeTransformationContext setModifier(GNode.Modifier modifier) {
        this.modifier = modifier;
        return this;
    }

    /**
     * Gets node.
     *
     * @return the node
     */
    public GNode getNode() {
        return node;
    }

    /**
     * Sets node.
     *
     * @param node the node
     * @return the node
     */
    public GNodeTransformationContext setNode(GNode node) {
        this.node = node;
        return this;
    }
}
