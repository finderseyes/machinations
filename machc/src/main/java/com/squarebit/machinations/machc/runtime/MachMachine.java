package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.ast.GProgram;

public final class MachMachine {
    private final GProgram program;
    private final TypeRegistry typeRegistry = new TypeRegistry();

    /**
     * Creates a new machine instance.
     */
    private MachMachine(final GProgram program) {
        this.program = program;
    }

    /**
     * Creates a new machine using given program specifications.
     * @param program the program specification.
     * @return a machine.
     */
    public MachMachine from(final GProgram program) {
        MachMachine machine = new MachMachine(program);
        return machine;
    }
}
