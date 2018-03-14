package com.squarebit.machinations.machc.ast;

import com.squarebit.machinations.machc.ast.expressions.GExpression;

/**
 * A member field declared in a graph.
 */
public class GField extends GGraphField {
    private GExpression initializer = null;

    /**
     * Gets initializer.
     *
     * @return the initializer
     */
    public GExpression getInitializer() {
        return initializer;
    }

    /**
     * Sets initializer.
     *
     * @param initializer the initializer
     * @return the initializer
     */
    public GField setInitializer(GExpression initializer) {
        this.initializer = initializer;
        return this;
    }
}
