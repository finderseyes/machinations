package com.squarebit.machinations.machc.simulation;

import com.squarebit.machinations.machc.avm.*;
import com.squarebit.machinations.machc.avm.runtime.TBoolean;
import com.squarebit.machinations.machc.avm.runtime.TRuntimeGraph;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Contains data regarding to a simulation pass.
 */
public class SimulationContext {
    private Simulation simulation;
    private Machine machine;
    private ModuleInfo moduleInfo;
    private TypeInfo mainGraphType;
    private TRuntimeGraph mainGraph;

    // Time
    private int time = -1;

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
    public CompletableFuture<Void> start() {
        this.machine.start();
        return createMainGraphIfNeeded().thenCompose(this::updateInteractiveMethodStatus);
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
        this.time++;
        return executeGraph(this.mainGraph).thenCompose(v -> updateInteractiveMethodStatus(this.mainGraph));
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

    /**
     * Update interactive status of interactive methods.
     * @param graph the graph.
     * @return
     */
    private CompletableFuture<Void> updateInteractiveMethodStatus(TRuntimeGraph graph) {
        TypeInfo typeInfo = graph.getTypeInfo();
        List<MethodInfo> runnableMethods = getRunnableMethods(typeInfo);
        return CompletableFuture.allOf(runnableMethods.stream()
                .filter(m -> m.getModifier() == MethodModifier.INTERACTIVE && m.getInteractiveCondition() != null)
                .map(m -> {
                    MachineInvocationPlan plan = new MachineInvocationPlan(m.getInteractiveCondition(), graph);
                    return machine.machineInvoke(plan).thenAccept(result -> {
                        TBoolean booleanResult = machine.getExpressionMachine().evaluateAsBoolean(result);
                        MethodRuntimeInfo runtimeInfo = graph.getMethodRuntimeInfo(m);
                        runtimeInfo.setActive(booleanResult.getValue());
                    });
                })
                .toArray(CompletableFuture[]::new)
        );
    }

    /**
     *
     * @param graph
     * @return
     */
    private CompletableFuture<Void> executeGraph(TRuntimeGraph graph) {
        TypeInfo typeInfo = graph.getTypeInfo();
        List<MethodInfo> runnableMethods = getRunnableMethods(typeInfo);

        return CompletableFuture.allOf(runnableMethods.stream()
                .filter(m -> this.canExecute(graph, m))
                .map(m -> {
                    MachineInvocationPlan plan = new MachineInvocationPlan(m, graph);
                    return machine.machineInvoke(plan);
                })
                .toArray(CompletableFuture[]::new)
        );
    }

    /**
     *
     * @param typeInfo
     * @return
     */
    private List<MethodInfo> getRunnableMethods(TypeInfo typeInfo) {
        return simulation.runnableMethodByType.computeIfAbsent(typeInfo, t ->
            typeInfo.getMethods().stream()
                    .filter(m -> !m.isConstructor() && !m.isInternal() && !m.isStatic())
                    .collect(Collectors.toList())
        );
    }

    /**
     *
     * @param graph
     * @param methodInfo
     * @return
     */
    private boolean canExecute(TRuntimeGraph graph, MethodInfo methodInfo) {
        MethodModifier modifier = methodInfo.getModifier();
        return (modifier == MethodModifier.AUTOMATIC ||
                (modifier == MethodModifier.INTERACTIVE && graph.getMethodRuntimeInfo(methodInfo).isActive()) ||
                (modifier == MethodModifier.START && this.time == 0)
        );
    }
}
