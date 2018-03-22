package com.squarebit.machinations.machc.ast;

import com.squarebit.machinations.machc.ast.expressions.GSetDescriptor;

/**
 * A node in the graph.
 */
public class GNode extends GGraphField {
    /**
     * The node type.
     */
    public static enum Type {
        POOL,
        SOURCE,
        DRAIN,
        TRANSITIVE,
        CONVERTER,
        END;

        public static Type parse(String value) {
            if (value.equals("pool"))
                return POOL;
            else if (value.equals("transitive"))
                return TRANSITIVE;
            else if (value.equals("source"))
                return SOURCE;
            else if (value.equals("drain"))
                return DRAIN;
            else if (value.equals("converter"))
                return CONVERTER;
            else if (value.equals("end"))
                return END;

            return POOL;
        }
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

    private Modifier modifier = new Modifier();
    private Type type = Type.POOL;
    private GSetDescriptor initializer = null;

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
    public GNode setType(Type type) {
        this.type = type;
        return this;
    }

    /**
     * Gets initializer.
     *
     * @return the initializer
     */
    public GSetDescriptor getInitializer() {
        return initializer;
    }

    /**
     * Sets initializer.
     *
     * @param initializer the initializer
     * @return the initializer
     */
    public GNode setInitializer(GSetDescriptor initializer) {
        this.initializer = initializer;
        return this;
    }
}
