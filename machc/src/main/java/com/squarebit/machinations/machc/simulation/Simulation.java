package com.squarebit.machinations.machc.simulation;

/**
 * Contains the whole information of the simulation process as well as its results.
 */
public class Simulation {
    private SimulationConfigurations configurations;

    /**
     * Instantiates a new Simulation.
     *
     * @param configurations the configurations
     */
    public Simulation(SimulationConfigurations configurations) {
        this.configurations = configurations;
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
