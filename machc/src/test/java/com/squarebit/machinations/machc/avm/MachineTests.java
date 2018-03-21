package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.Utils;
import com.squarebit.machinations.machc.avm.runtime.*;
import com.squarebit.machinations.machc.avm.runtime.nodes.TPoolNode;
import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MachineTests {
    @Test
    public void specs_001() throws Exception {
        ModuleInfo module = Utils.compile("specs/specs-001.mach");

        Machine machine = new Machine(module);
        machine.start();

        {
            TypeInfo typeInfo = module.findType("a");
            TObject graph = machine.newInstance(typeInfo).get();

            assertThat(graph.getClass()).isEqualTo(TRuntimeGraph.class);

            {
                assertThat(typeInfo.findField("_int").get(graph).getClass()).isEqualTo(TInteger.class);
                assertThat(((TInteger)typeInfo.findField("_int").get(graph)).getValue()).isEqualTo(10);
            }

            {
                assertThat(typeInfo.findField("_float").get(graph).getClass()).isEqualTo(TFloat.class);
                assertThat(((TFloat)typeInfo.findField("_float").get(graph)).getValue()).isEqualTo(1.5f);
            }

            {
                assertThat(typeInfo.findField("_float_as_percentage").get(graph).getClass()).isEqualTo(TFloat.class);
                assertThat(((TFloat)typeInfo.findField("_float_as_percentage").get(graph)).getValue())
                        .isCloseTo(0.1f, Offset.offset(1e-3f));
            }

            {
                assertThat(typeInfo.findField("_dice").get(graph).getClass()).isEqualTo(TInteger.class);
                assertThat(((TInteger)typeInfo.findField("_dice").get(graph)).getValue()).isBetween(1, 20);
            }

            {
                assertThat(typeInfo.findField("_draw").get(graph).getClass()).isEqualTo(TInteger.class);
                assertThat(((TInteger)typeInfo.findField("_draw").get(graph)).getValue()).isBetween(1, 2);
            }
        }

        {
            TypeInfo typeInfo = module.findType("b");
            TObject graph = machine.newInstance(typeInfo).get();

            assertThat(graph.getClass()).isEqualTo(TRuntimeGraph.class);

            {
                assertThat(typeInfo.findField("_boolean").get(graph).getClass()).isEqualTo(TBoolean.class);
                assertThat(((TBoolean)typeInfo.findField("_boolean").get(graph)).getValue()).isEqualTo(true);
            }

            {
                assertThat(typeInfo.findField("_string").get(graph).getClass()).isEqualTo(TString.class);
                assertThat(((TString)typeInfo.findField("_string").get(graph)).getValue()).isEqualTo("hello Mach");
            }
        }

        {
            TypeInfo typeInfo = module.findType("c");
            TObject graph = machine.newInstance(typeInfo).get();

            assertThat(graph.getClass()).isEqualTo(TRuntimeGraph.class);

            {
                assertThat(typeInfo.findField("a_1").get(graph).getClass()).isEqualTo(TInteger.class);
                assertThat(((TInteger)typeInfo.findField("a_1").get(graph)).getValue()).isEqualTo(3);
            }

            {
                assertThat(typeInfo.findField("a_2").get(graph).getClass()).isEqualTo(TInteger.class);
                assertThat(((TInteger)typeInfo.findField("a_2").get(graph)).getValue()).isEqualTo(13);
            }

            {
                assertThat(typeInfo.findField("a_3").get(graph).getClass()).isEqualTo(TSetDescriptor.class);

                TSetDescriptor a_3 = (TSetDescriptor)typeInfo.findField("a_3").get(graph);
                assertThat(a_3.findElementTypeDescriptor("").getSize()).isEqualTo(5);
                assertThat(a_3.findElementTypeDescriptor("gold").getSize()).isEqualTo(10);
            }
        }

        machine.shutdown();
    }

    @Test
    public void specs_002() throws Exception {
        ModuleInfo module = Utils.compile("specs/specs-002.mach");

        Machine machine = new Machine(module);
        machine.start();

        {
            TypeInfo typeInfo = module.findType("game");
            TObject graph = machine.newInstance(typeInfo).get();

            assertThat(graph.getClass()).isEqualTo(TRuntimeGraph.class);

            {
                TPoolNode pool = (TPoolNode)typeInfo.findField("a_0").get(graph);
                assertThat(pool.getContent().size()).isEqualTo(0);
            }

            {
                TPoolNode pool = (TPoolNode)typeInfo.findField("a_1").get(graph);
                assertThat(pool.getContent().size()).isEqualTo(10);
            }

            {
                TPoolNode pool = (TPoolNode)typeInfo.findField("a_2").get(graph);
                assertThat(pool.getContent().size()).isEqualTo(15);
            }

            {
                TPoolNode pool = (TPoolNode)typeInfo.findField("a_3").get(graph);
                assertThat(pool.getContent().size()).isEqualTo(20);
            }
        }

        machine.shutdown();
    }
}
