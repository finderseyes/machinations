package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.Utils;
import com.squarebit.machinations.machc.avm.runtime.*;
import com.squarebit.machinations.machc.avm.runtime.nodes.*;
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
            TRuntimeGraph graph = (TRuntimeGraph)machine.newInstance(typeInfo).get();

            {
                TPoolNode pool = (TPoolNode)graph.getNode("a_0");
                assertThat(pool.getContent().size()).isEqualTo(0);
                assertThat(pool.getGraph()).isEqualTo(graph);
            }

            {
                TPoolNode pool = (TPoolNode)typeInfo.findField("a_1").get(graph);
                assertThat(pool.getContent().size()).isEqualTo(10);
                assertThat(pool.getContent().size("soldier")).isEqualTo(10);
                assertThat(pool.getGraph()).isEqualTo(graph);
            }

            {
                TPoolNode pool = (TPoolNode)typeInfo.findField("a_2").get(graph);
                assertThat(pool.getContent().size()).isEqualTo(15);
                assertThat(pool.getContent().size("gold")).isEqualTo(10);
                assertThat(pool.getContent().size("")).isEqualTo(5);
                assertThat(pool.getGraph()).isEqualTo(graph);
            }

            {
                TPoolNode pool = (TPoolNode)typeInfo.findField("a_3").get(graph);
                assertThat(pool.getContent().size()).isEqualTo(25);
                assertThat(pool.getContent().size("soldier")).isEqualTo(20);
                assertThat(pool.getContent().size("gold")).isEqualTo(5);
                assertThat(pool.getGraph()).isEqualTo(graph);
            }

            {
                TTransitiveNode transitive = (TTransitiveNode)typeInfo.findField("a_4").get(graph);
                assertThat(transitive).isNotNull();
                assertThat(transitive.getGraph()).isEqualTo(graph);
            }

            {
                TSourceNode node = (TSourceNode) typeInfo.findField("a_5").get(graph);
                assertThat(node).isNotNull();
                assertThat(node.getGraph()).isEqualTo(graph);
            }

            {
                TDrainNode node = (TDrainNode) typeInfo.findField("a_6").get(graph);
                assertThat(node).isNotNull();
                assertThat(node.getGraph()).isEqualTo(graph);
            }

            {
                TConverterNode node = (TConverterNode) typeInfo.findField("a_7").get(graph);
                assertThat(node).isNotNull();
                assertThat(node.getGraph()).isEqualTo(graph);
            }

            {
                TEndNode node = (TEndNode) typeInfo.findField("a_8").get(graph);
                assertThat(node).isNotNull();
            }

            {
                TGraphNode node = (TGraphNode)typeInfo.findField("a_9").get(graph);
                assertThat(node).isNotNull();
                assertThat(node.getTypeInfo().getGraphType()).isEqualTo(module.findType("soldier"));

                TypeInfo graphType = node.getTypeInfo().getGraphType();
                TRuntimeGraph subGraph = node.getGraph();
                assertThat(((TInteger)graphType.findField("shield").get(subGraph)).getValue()).isEqualTo(10);
                assertThat(((TInteger)graphType.findField("damage").get(subGraph)).getValue()).isEqualTo(2);
            }

            {
                TConnection connection = graph.getConnection("e_0");
                assertThat(connection.getFrom()).isEqualTo(graph.getNode("a_0"));
                assertThat(connection.getTo()).isEqualTo(graph.getNode("a_1"));
            }
        }

        machine.shutdown();
    }

    @Test
    public void specs_003() throws Exception {
        ModuleInfo module = Utils.compile("specs/specs-003.mach");

        Machine machine = new Machine(module);
        machine.start();

        TypeInfo typeInfo = module.findType("main");
        TRuntimeGraph graph = (TRuntimeGraph)machine.newInstance(typeInfo).get();

        {
            MethodInfo methodInfo = typeInfo.findMethod("f0");
            assertThat(methodInfo.getParameterCount()).isEqualTo(1);

            {
                TBoolean result = (TBoolean)machine.machineInvoke(
                        new MachineInvocationPlan(methodInfo, graph, new TInteger(1))
                ).get();
                assertThat(result).isEqualTo(TBoolean.TRUE);
            }

            {
                TBoolean result = (TBoolean)machine.machineInvoke(
                        new MachineInvocationPlan(methodInfo, graph, new TInteger(-1))
                ).get();
                assertThat(result).isEqualTo(TBoolean.FALSE);
            }
        }

        {
            MethodInfo methodInfo = typeInfo.findMethod("f1");
            assertThat(methodInfo.getParameterCount()).isEqualTo(1);

            {
                TBoolean result = (TBoolean)machine.machineInvoke(
                        new MachineInvocationPlan(methodInfo, graph, new TInteger(1))
                ).get();
                assertThat(result).isEqualTo(TBoolean.TRUE);
            }

            {
                TBoolean result = (TBoolean)machine.machineInvoke(
                        new MachineInvocationPlan(methodInfo, graph, new TInteger(-1))
                ).get();
                assertThat(result).isEqualTo(TBoolean.FALSE);
            }
        }

        machine.shutdown();
    }
}
