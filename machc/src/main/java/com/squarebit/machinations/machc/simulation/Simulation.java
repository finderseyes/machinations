package com.squarebit.machinations.machc.simulation;

import com.squarebit.machinations.machc.avm.MethodInfo;
import com.squarebit.machinations.machc.avm.TypeInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the whole information of the simulation process as well as its results.
 */
public class Simulation {
    private SimulationConfigurations configurations;

    Map<TypeInfo, List<MethodInfo>> runnableMethodByType;

    /**
     * Instantiates a new Simulation.
     *
     * @param configurations the configurations
     */
    public Simulation(SimulationConfigurations configurations) {
        this.configurations = configurations;
        this.runnableMethodByType = new HashMap<>();
    }

    /**
     * Gets configurations.
     *
     * @return the configurations
     */
    public SimulationConfigurations getConfigurations() {
        return configurations;
    }

    /**
     *
     * @return
     */
    public SimulationContext simulateOnePass() {
        SimulationContext context = new SimulationContext(this);
        context.simulateOneTimeStep();
        return context;
    }
}
