package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;

/**
 * A variable declared in a frame.
 */
public final class Variable {
    public static class Builder {
        private Frame.Builder frameBuilder;
        private TType type = TType.OBJECT_TYPE;

        /**
         * Instantiates a new variable builder.
         *
         * @param frameBuilder the owning frame builder
         */
        public Builder(Frame.Builder frameBuilder) {
            this.frameBuilder = frameBuilder;
        }

        /**
         * Sets type.
         *
         * @param type the type
         * @return the type
         */
        public Builder setType(TType type) {
            this.type = type;
            return this;
        }

        /**
         * Builds the variable.
         * @return the variable.
         */
        public Variable build() {
            Variable variable = new Variable();
            variable.type = type;

            this.frameBuilder.variables.add(variable);
            return variable;
        }
    }

    protected Frame frame;
    protected int index;
    private TType type;

    /**
     * Gets the declaring frame.
     *
     * @return the declaring frame
     */
    public Frame getFrame() {
        return frame;
    }

    /**
     * Gets the variable index when declared in the frame.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public TType getType() {
        return type;
    }

    /**
     * Gets the variable value under given activation frame.
     * @return the variable value
     */
    public TObject get(FrameActivation activation) {
        if (activation == null)
            throw new RuntimeException("Variable not found under given activation.");

        if (activation.frame == frame)
            return activation.variableTable[index];
        else
            return get(activation.parent);
    }

    /**
     * Sets variable value under given activation frame.
     *
     * @param activation the activation
     * @param value      the value
     */
    public void set(FrameActivation activation, TObject value) {
        if (activation == null)
            throw new RuntimeException("Variable not found under given activation.");

        if (activation.frame == frame)
            activation.variableTable[index] = value;
        else
            set(activation.parent, value);
    }
}
