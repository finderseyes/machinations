package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.Utils;
import com.squarebit.machinations.machc.runtime.components.TField;
import com.squarebit.machinations.machc.runtime.components.TRuntimeGraph;
import com.squarebit.machinations.machc.runtime.components.TType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MachMachineTests {
    @Test
    public void specs_001() throws Exception {
        MachMachine machine = Utils.compileToMachine("specs/specs-001.mach");

        {
            TType typeA = machine.getType("a");

            assertThat(typeA.getName()).isEqualTo("a");
            assertThat(typeA.getBaseType()).isEqualTo(TType.GRAPH_TYPE);
            assertThat(typeA.getImplementation()).isEqualTo(TRuntimeGraph.class);

            TField _int = typeA.getField("_int");
            assertThat(_int.getName()).isEqualTo("_int");
            assertThat(_int.getType()).isEqualTo(TType.INTEGER_TYPE);
        }
    }

//    @Test
//    public void should_create_machine_with_graphs() {
//        GGraph graph = new GGraph();
//        graph.setId("GameGraph");
//
//        GUnit unit = new GUnit();
//        unit.addGraph(graph);
//
//        GProgram program = new GProgram();
//        program.addUnit(unit);
//
//        MachMachine machine = MachMachine.from(program);
//
//        TType graphType = machine.getType("GameGraph");
//
//        assertThat(graphType.getName()).isEqualTo("GameGraph");
//        assertThat(graphType.getBaseType()).isEqualTo(TType.GRAPH_TYPE);
//    }
//
//    @Test
//    public void should_instantiate_object_of_given_type() throws Exception {
//        GGraph graph = new GGraph();
//        graph.setId("GameGraph");
//
//        GUnit unit = new GUnit();
//        unit.addGraph(graph);
//
//        GProgram program = new GProgram();
//        program.addUnit(unit);
//
//        MachMachine machine = MachMachine.from(program);
//
//        machine.executeNext();
//        machine.executeNext();
//        machine.executeNext();
//        machine.executeNext();
//        machine.executeNext();
//
//        TType graphType = machine.getType("GameGraph");
//
//        TRuntimeGraph runtimeGraph = (TRuntimeGraph)graphType.newInstance();
//        assertThat(runtimeGraph.getClass()).isEqualTo(TRuntimeGraph.class);
//        assertThat(runtimeGraph.getType()).isEqualTo(graphType);
//    }
//
//    @Test
//    public void should_execute_frame() throws Exception {
//        Frame.Builder frameBuidler = new Frame.Builder();
//        Variable v0 = frameBuidler.createVariable().setType(TType.INTEGER_TYPE).build();
//        Frame frame = frameBuidler.build();
//
//        FrameActivation activation = frame.activate();
//        int k = 10;
//    }
}
