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
        TRASITIVE,
        CONVERTER,
        END
    }

    /**
     * The modifier.
     */
    public static class Modifier {
        private boolean transitive = false;
        private boolean interative = false;

        /**
         * Is transitive boolean.
         *
         * @return the boolean
         */
        public boolean isTransitive() {
            return transitive;
        }

        /**
         * Sets transitive.
         *
         * @param transitive the transitive
         * @return the transitive
         */
        public Modifier setTransitive(boolean transitive) {
            this.transitive = transitive;
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
        private GResourceSet resourceSet;

        /**
         * Gets resource set.
         *
         * @return the resource set
         */
        public GResourceSet getResourceSet() {
            return resourceSet;
        }

        /**
         * Sets resource set.
         *
         * @param resourceSet the resource set
         * @return the resource set
         */
        public Initializer setResourceSet(GResourceSet resourceSet) {
            this.resourceSet = resourceSet;
            return this;
        }
    }

    public static final Initializer SOURCE_INITIALIZER = new Initializer();
    public static final Initializer DRAIN_INITIALIZER = new Initializer();

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
