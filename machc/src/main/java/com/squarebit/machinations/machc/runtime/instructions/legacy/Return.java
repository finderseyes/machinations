package com.squarebit.machinations.machc.runtime.instructions.legacy;

import com.squarebit.machinations.machc.runtime.Frame;
import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.Instruction;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TVoid;

public final class Return extends Instruction {
    private final TObject value;

    /**
     * Instantiates a new return instruction.
     *
     * @param value the value to return
     */
    public Return(TObject value) {
        this.value = value;
    }

    /**
     * Instantiates a new Return.
     */
    public Return() {
        this.value = TVoid.INSTANCE;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public TObject getValue() {
        return value;
    }

    /**
     * Execute the current instruction.
     */
    @Override
    public void execute() {
        Frame frame = getFrame();
        FrameActivation activation = getFrame().currentActivation();
        frame.getMethodReturnValue().set(activation, this.value);
    }
}
