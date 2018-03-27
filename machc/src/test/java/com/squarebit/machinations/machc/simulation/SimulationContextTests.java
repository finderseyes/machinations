package com.squarebit.machinations.machc.simulation;

import com.squarebit.machinations.machc.Utils;
import com.squarebit.machinations.machc.avm.*;
import com.squarebit.machinations.machc.avm.runtime.TInteger;
import com.squarebit.machinations.machc.avm.runtime.TRuntimeGraph;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimulationContextTests {
    @Test
    public void should_initiate_correct_modifiers() throws Exception {
        ModuleInfo module = Utils.compile("simulation/test-001.mach");
        TypeInfo main = module.findType("main");

        {
            MethodInfo methodInfo = main.findMethod("f0");
            assertThat(methodInfo.getModifier()).isEqualTo(MethodModifier.AUTOMATIC);
        }

        {
            MethodInfo methodInfo = main.findMethod("f1");
            assertThat(methodInfo.getModifier()).isEqualTo(MethodModifier.START);
        }

        {
            MethodInfo methodInfo = main.findMethod("f2");
            assertThat(methodInfo.getModifier()).isEqualTo(MethodModifier.INTERACTIVE);
            assertThat(methodInfo.getInteractiveCondition()).isNotNull();
        }
    }


    @Test
    public void should_execute_automatic_functions() throws Exception {
        ModuleInfo module = Utils.compile("simulation/test-001.mach");
        TypeInfo main = module.findType("main");

        Simulation simulation = new Simulation(new SimulationConfigurations()
                .setModuleInfo(module).setMainGraphType(module.findType("main")));

        SimulationContext context = new SimulationContext(simulation);
        context.start().get();

        // time step 0
        {
            MethodInfo methodInfo = main.findMethod("f0");
            MethodRuntimeInfo runtimeInfo = context.getMainGraph().getMethodRuntimeInfo(methodInfo);
            assertThat(runtimeInfo.isActive()).isTrue();
        }

        {
            MethodInfo methodInfo = main.findMethod("f1");
            MethodRuntimeInfo runtimeInfo = context.getMainGraph().getMethodRuntimeInfo(methodInfo);
            assertThat(runtimeInfo.isActive()).isTrue();
        }

        {
            MethodInfo methodInfo = main.findMethod("f2");
            MethodRuntimeInfo runtimeInfo = context.getMainGraph().getMethodRuntimeInfo(methodInfo);
            assertThat(runtimeInfo.isActive()).isFalse();
        }


        // time step 1.
        context.simulateOneTimeStep().get();

        {
            MethodInfo methodInfo = main.findMethod("f0");
            MethodRuntimeInfo runtimeInfo = context.getMainGraph().getMethodRuntimeInfo(methodInfo);
            assertThat(runtimeInfo.isActive()).isTrue();
        }

        {
            MethodInfo methodInfo = main.findMethod("f1");
            MethodRuntimeInfo runtimeInfo = context.getMainGraph().getMethodRuntimeInfo(methodInfo);
            assertThat(runtimeInfo.isActive()).isTrue();
        }

        {
            MethodInfo methodInfo = main.findMethod("f2");
            MethodRuntimeInfo runtimeInfo = context.getMainGraph().getMethodRuntimeInfo(methodInfo);
            assertThat(runtimeInfo.isActive()).isTrue();
        }

        assertThat(((TInteger)context.getMainGraph().getField("x")).getValue()).isEqualTo(1);
        assertThat(((TInteger)context.getMainGraph().getField("y")).getValue()).isEqualTo(1);


        // time step 2
        context.simulateOneTimeStep().get();

        assertThat(((TInteger)context.getMainGraph().getField("x")).getValue()).isEqualTo(2);
        assertThat(((TInteger)context.getMainGraph().getField("y")).getValue()).isEqualTo(1);


        context.stop();
    }

    @Test
    public void should_simulate_default_graph() throws Exception {
        ModuleInfo module = Utils.compile("simulation/test-001.mach");

        Simulation simulation = new Simulation(new SimulationConfigurations().setModuleInfo(module));
        SimulationContext context = new SimulationContext(simulation);

        context.start().get();

        TRuntimeGraph graph = context.getMainGraph();
        assertThat(graph.getTypeInfo().getName()).isEqualTo("default_graph");

        context.stop();
    }
}
