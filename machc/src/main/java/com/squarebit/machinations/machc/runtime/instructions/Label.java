package com.squarebit.machinations.machc.runtime.instructions;

import com.squarebit.machinations.machc.runtime.MachInstruction;

/**
 * Marks a position in the instruction set.
 */
public final class Label extends MachInstruction {
    private final String name;

    /**
     * Instantiates a new Label.
     */
    public Label() {
        this.name = null;
    }

    /**
     * Instantiates a new Label.
     *
     * @param name the name
     */
    public Label(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
