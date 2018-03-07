package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.ast.GGraph;
import com.squarebit.machinations.machc.ast.GProgram;
import com.squarebit.machinations.machc.ast.GUnit;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MachMachineTests {
    @Test
    public void should_create_machine_with_graphs() {
        GGraph graph = new GGraph();
        graph.setId("GameGraph");

        GUnit unit = new GUnit();
        unit.addGraph(graph);

        GProgram program = new GProgram();
        program.addUnit(unit);

        MachMachine machine = MachMachine.from(program);

        TType graphType = machine.getType("GameGraph");

        assertThat(graphType.getName()).isEqualTo("GameGraph");
        assertThat(graphType.getBaseType()).isEqualTo(BuiltinTypes.GRAPH_TYPE);
    }

    @Test
    public void should_instantiate_object_of_given_type() throws Exception {
        GGraph graph = new GGraph();
        graph.setId("GameGraph");

        GUnit unit = new GUnit();
        unit.addGraph(graph);

        GProgram program = new GProgram();
        program.addUnit(unit);

        MachMachine machine = MachMachine.from(program);

        TType graphType = machine.getType("GameGraph");

        TRuntimeGraph runtimeGraph = (TRuntimeGraph)graphType.newInstance();
        assertThat(runtimeGraph.getClass()).isEqualTo(TRuntimeGraph.class);
        assertThat(runtimeGraph.getType()).isEqualTo(graphType);
    }
}
