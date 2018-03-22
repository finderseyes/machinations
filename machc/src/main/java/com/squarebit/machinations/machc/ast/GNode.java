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
        private boolean isInput = false;
        private boolean isOutput = false;

        /**
         * Determines if the node is default input node of the container graph (used when container graph is
         * a sub-graph of another graph).
         *
         * @return the boolean
         */
        public boolean isInput() {
            return isInput;
        }

        /**
         * Sets input.
         *
         * @param input the input
         * @return the input
         */
        public Modifier setInput(boolean input) {
            isInput = input;
            return this;
        }

        /**
         * Determines if the node is default output node of the container graph.
         *
         * @return the boolean
         */
        public boolean isOutput() {
            return isOutput;
        }

        /**
         * Sets output.
         *
         * @param output the output
         * @return the output
         */
        public Modifier setOutput(boolean output) {
            isOutput = output;
            return this;
        }
    }

    private Modifier modifier = new Modifier();
    private GNodeType type = new GNodeType(Type.POOL);
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
    public GNodeType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     * @return the type
     */
    public GNode setType(GNodeType type) {
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
