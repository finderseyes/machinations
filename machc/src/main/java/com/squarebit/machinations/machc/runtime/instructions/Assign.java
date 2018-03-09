package com.squarebit.machinations.machc.runtime.instructions;

import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.Instruction;
import com.squarebit.machinations.machc.runtime.Variable;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.expressions.TObjectRef;


/**
 * Assign one variable to another.
 */
public final class Assign extends Instruction {
    private final Variable from, to;

    /**
     * Instantiates a new assign instruction.
     *
     */
    public Assign(final Variable from, final Variable to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public Variable getFrom() {
        return from;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public Variable getTo() {
        return to;
    }

    /**
     * Execute the current instruction.
     */
    @Override
    public void execute() {
        FrameActivation activation = this.getFrame().currentActivation();
        TObject value = from.get(activation);
        TObject castedValue = new TObjectRef(value).evalTo(to.getType(), activation);
        to.set(activation, castedValue);
    }
}
