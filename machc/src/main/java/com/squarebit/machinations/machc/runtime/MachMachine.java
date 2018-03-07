package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.ast.GGraph;
import com.squarebit.machinations.machc.ast.GProgram;
import com.squarebit.machinations.machc.ast.GUnit;

public final class MachMachine {
    private final GProgram program;
    private final TypeRegistry typeRegistry = new TypeRegistry();

    /**
     * Creates a new machine instance.
     */
    private MachMachine(final GProgram program) {
        this.program = program;
        this.compile();
    }

    /**
     * Creates a new machine using given program specifications.
     * @param program the program specification.
     * @return a machine.
     */
    public static MachMachine from(final GProgram program) {
        MachMachine machine = new MachMachine(program);
        return machine;
    }

    /**
     * Gets a type using its name.
     *
     * @param typeName the type name
     * @return the type
     */
    public TType getType(String typeName) {
        return typeRegistry.getType(typeName);
    }

    /**
     * Compiles current program.
     */
    private void compile() {
        for (GUnit unit: program.getUnits()) {
            for (GGraph graph: unit.getGraphs()) {
                TType<TRuntimeGraph> type = new TType<>(graph.getId(), BuiltinTypes.GRAPH_TYPE, TRuntimeGraph.class);
                typeRegistry.registerType(type);
            }
        }
    }
}
