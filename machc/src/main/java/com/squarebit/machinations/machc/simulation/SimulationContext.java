package com.squarebit.machinations.machc.simulation;

import com.squarebit.machinations.machc.avm.Machine;
import com.squarebit.machinations.machc.avm.ModuleInfo;
import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.runtime.TRuntimeGraph;

import java.util.concurrent.CompletableFuture;

/**
 * Contains data regarding to a simulation pass.
 */
public class SimulationContext {
    private Simulation simulation;
    private Machine machine;
    private ModuleInfo moduleInfo;
    private TypeInfo mainGraphType;
    private TRuntimeGraph mainGraph;

    /**
     * Instantiates a new Simulation context.
     *
     * @param simulation the simulation
     */
    public SimulationContext(Simulation simulation) {
        this.simulation = simulation;

        this.moduleInfo = simulation.getConfigurations().getModuleInfo();
        this.mainGraphType = simulation.getConfigurations().getMainGraphType();
        this.machine = new Machine(moduleInfo);
    }

    /**
     * Start.
     */
    public void start() {
        this.machine.start();
    }

    /**
     * Stop.
     */
    public void stop() {
        this.machine.shutdown();
    }

    /**
     * Gets main graph.
     *
     * @return the main graph
     */
    public TRuntimeGraph getMainGraph() {
        return mainGraph;
    }

    public CompletableFuture<Void> simulateOneTimeStep() {
        return createMainGraphIfNeeded().thenCompose(graph -> {
            CompletableFuture<Void> returnFuture = new CompletableFuture<>();

            returnFuture.complete(null);

            return returnFuture;
        });
    }

    /**
     * Creates the main graph if needed.
     * @return the main graph.
     */
    private CompletableFuture<TRuntimeGraph> createMainGraphIfNeeded() {
        if (this.mainGraph == null)
            return this.machine.newInstance(this.mainGraphType).thenApply(v -> {
                this.mainGraph = (TRuntimeGraph)v;
                return this.mainGraph;
            });
        else
            return CompletableFuture.completedFuture(this.mainGraph);
    }
}
