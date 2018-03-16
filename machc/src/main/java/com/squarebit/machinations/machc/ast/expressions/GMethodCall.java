package com.squarebit.machinations.machc.ast.expressions;

/**
 * The type G method call.
 */
public final class GMethodCall extends GExpression {
    private final GSymbolRef target;
    private final String name;
    private final GExpression[] arguments;

    /**
     * Instantiates a new G method call.
     *
     * @param target    the target
     * @param name      the name
     * @param arguments the arguments
     */
    public GMethodCall(GSymbolRef target, String name, GExpression[] arguments) {
        this.target = target;
        this.name = name;
        this.arguments = arguments;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public GSymbolRef getTarget() {
        return target;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get arguments g expression [ ].
     *
     * @return the g expression [ ]
     */
    public GExpression[] getArguments() {
        return arguments;
    }
}
