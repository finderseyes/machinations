package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.Utils;
import com.squarebit.machinations.machc.vm.components.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MachMachineTests {
    @Test
    public void specs_001() throws Exception {
        ProgramInfo program = Utils.compile("specs/specs-001.mach");

        MachMachine machine = new MachMachine(program);
        machine.start();

        {
            TypeInfo typeInfo = program.findType("a").get(0);
            TObject graph = machine.newInstance(typeInfo).get();

            assertThat(graph.getClass()).isEqualTo(TRuntimeGraph.class);
            assertThat(Types.RUNTIME_GRAPH_TYPE.isAssignableFrom(graph.getType())).isTrue();

            {
                assertThat(typeInfo.findField("_int").get(graph).getClass()).isEqualTo(TInteger.class);
                assertThat(((TInteger)typeInfo.findField("_int").get(graph)).getValue()).isEqualTo(10);
            }

            {
                assertThat(typeInfo.findField("_float").get(graph).getClass()).isEqualTo(TFloat.class);
                assertThat(((TFloat)typeInfo.findField("_float").get(graph)).getValue()).isEqualTo(1.5f);
            }

            {
                assertThat(typeInfo.findField("_dice").get(graph).getClass()).isEqualTo(TRandomDice.class);
                assertThat(((TRandomDice)typeInfo.findField("_dice").get(graph)).getTimes()).isEqualTo(2);
                assertThat(((TRandomDice)typeInfo.findField("_dice").get(graph)).getFaces()).isEqualTo(10);
            }

            {
                assertThat(typeInfo.findField("_draw").get(graph).getClass()).isEqualTo(TRandomDice.class);
                assertThat(((TRandomDice)typeInfo.findField("_draw").get(graph)).getTimes()).isEqualTo(1);
                assertThat(((TRandomDice)typeInfo.findField("_draw").get(graph)).getFaces()).isEqualTo(20);
            }
        }

        {
            TypeInfo typeInfo = program.findType("b").get(0);
            TObject graph = machine.newInstance(typeInfo).get();

            assertThat(graph.getClass()).isEqualTo(TRuntimeGraph.class);
            assertThat(Types.RUNTIME_GRAPH_TYPE.isAssignableFrom(graph.getType())).isTrue();

            {
                assertThat(typeInfo.findField("_boolean").get(graph).getClass()).isEqualTo(TBoolean.class);
                assertThat(((TBoolean)typeInfo.findField("_boolean").get(graph)).getValue()).isEqualTo(true);
            }

            {
                assertThat(typeInfo.findField("_string").get(graph).getClass()).isEqualTo(TString.class);
                assertThat(((TString)typeInfo.findField("_string").get(graph)).getValue()).isEqualTo("hello Mach");
            }
        }

        machine.shutdown();
    }
}
