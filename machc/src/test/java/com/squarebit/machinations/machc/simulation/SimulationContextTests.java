package com.squarebit.machinations.machc.simulation;

import com.squarebit.machinations.machc.Utils;
import com.squarebit.machinations.machc.avm.ModuleInfo;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimulationContextTests {
    @Test
    public void should_execute_automatic_functions() throws Exception {
        ModuleInfo module = Utils.compile("specs/specs-003.mach");
        Simulation simulation = new Simulation(new SimulationConfigurations()
                .setModuleInfo(module).setMainGraphType(module.findType("main")));

        SimulationContext context = new SimulationContext(simulation);
        context.start();

        context.simulateOneTimeStep().get();

        assertThat(context.getMainGraph()).isNotNull();

        context.stop();
    }
}
