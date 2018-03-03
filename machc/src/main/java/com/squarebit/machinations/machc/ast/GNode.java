package com.squarebit.machinations.machc.ast;

/**
 * A node in the graph.
 */
public class GNode extends GGraphElement {
    /**
     * The node type.
     */
    public static enum Type {
        POOL,
        SOURCE,
        DRAIN,
        END
    }

    /**
     * The modifier.
     */
    public static class Modifier {
        private Type type = Type.POOL;
        private boolean interative = false;

        /**
         * Gets type.
         *
         * @return the type
         */
        public Type getType() {
            return type;
        }

        /**
         * Sets type.
         *
         * @param type the type
         * @return the type
         */
        public Modifier setType(Type type) {
            this.type = type;
            return this;
        }

        /**
         * Is interative boolean.
         *
         * @return the boolean
         */
        public boolean isInterative() {
            return interative;
        }

        /**
         * Sets interative.
         *
         * @param interative the interative
         * @return the interative
         */
        public Modifier setInterative(boolean interative) {
            this.interative = interative;
            return this;
        }
    }

    /**
     * The node initializer.
     */
    public static class Initializer {

    }

    private Modifier modifier = new Modifier();
    private Initializer initializer = null;

    /**
     * Gets the node modifier.
     *
     * @return the modifier
     */
    public Modifier getModifier() {
        return modifier;
    }

    /**
     * Sets the node modifier.
     *
     * @param modifier the modifier
     * @return the modifier
     */
    public GNode setModifier(Modifier modifier) {
        this.modifier = modifier;
        return this;
    }

    /**
     * Gets initializer.
     *
     * @return the initializer
     */
    public Initializer getInitializer() {
        return initializer;
    }

    /**
     * Sets initializer.
     *
     * @param initializer the initializer
     * @return the initializer
     */
    public GNode setInitializer(Initializer initializer) {
        this.initializer = initializer;
        return this;
    }
}
