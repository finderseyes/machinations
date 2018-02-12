package com.squarebit.machinations.models;

import java.util.*;
import java.util.stream.Collectors;

public class MachinationsContext {
    public static final String DEFAULT_RESOURCE_NAME = "";

    private Configs configs = new Configs();
    private Set<AbstractElement> elements;
    private Map<String, AbstractElement> elementById;

    private int previousSimulatedTime = -1;
    private int time = -1;  // The time index now, negative means no simulation step has been executed.
    private int remainedActionPoints = -1;
    private Set<AbstractNode> activeNodes = new HashSet<>();
    private Set<AbstractNode> automaticNodes;

    public MachinationsContext() {
        this.elements = new HashSet<>();
        this.elementById = new HashMap<>();
    }

    /**
     * Find by id abstract element.
     *
     * @param id the id
     * @return the abstract element
     */
    public AbstractElement findById(String id) {
        return this.elementById.get(id);
    }

    /**
     * Gets elements.
     *
     * @return the elements
     */
    public Set<AbstractElement> getElements() {
        return elements;
    }

    /**
     * Addition element.
     *
     * @param element the element
     * @throws Exception the exception
     */
    protected void addElement(AbstractElement element) throws Exception {
        if (!this.elementById.containsKey(element.getId())) {
            this.elements.add(element);
            this.elementById.put(element.getId(), element);
        }
        else {
            throw new Exception(String.format("An element with id %s is already existed.", element.getId()));
        }
    }

    /**
     * Performs one simulation step.
     */
    public void simulateOneTimeStep() {
        this.initializeIfNeeded();

        if (this.previousSimulatedTime < this.time) {
            this.doSimulateOneTimeStep();
            this.previousSimulatedTime = this.time;
        }

        if (configs.getTimeMode() == TimeMode.TURN_BASED) {
            if (this.remainedActionPoints <= 0) {
                this.remainedActionPoints = this.configs.getActionPointsPerTurn();
                this.time++;
            }
        }
        else
            this.time++;
    }

    /**
     *
     */
    private void doSimulateOneTimeStep() {
        // Generate candidate activate connections.
        Set<ResourceConnection> activeConnections = this.activeNodes.stream()
                .map(this::getActiveConnection).flatMap(Set::stream).map(e -> (ResourceConnection)e)
                .collect(Collectors.toSet());

        // Flow rate this time step.
        Map<ResourceConnection, Integer> flowRates = new HashMap<>();
        activeConnections.forEach(c -> flowRates.put(c, c.getFlowRate()));

        //
        Map<ResourceConnection, Boolean> satisfiedConnections;


//        // Resolve synchronous pulling conflicts.
//        if (configs.getTimeMode() == TimeMode.SYNCHRONOUS) {
//
//        }
//        else
//            activeNodes.forEach(node -> node.activate(this.time));
    }

    private Set<AbstractConnection> getActiveConnection(AbstractNode node) {
        FlowMode mode = node.getFlowMode();
        if (mode == FlowMode.AUTOMATIC) {
            if (node.getIncomingConnections().size() == 0)
                return node.getOutgoingConnections();
            else
                return node.getIncomingConnections();
        }
        else if (mode == FlowMode.PULL_ALL || mode == FlowMode.PULL_ANY)
            return node.getIncomingConnections();
        else
            return node.getOutgoingConnections();
    }

    /**
     * Initializes the context if needed before simulation.
     */
    private void initializeIfNeeded() {
        // already simulated at least one step, no need to initialize.
        if (this.time >= 0)
            return;

        this.automaticNodes = automaticNodes = elements.stream()
                .filter(e -> e instanceof AbstractNode &&
                        ((AbstractNode)e).getActivationMode() == ActivationMode.AUTOMATIC
                ).map(e -> (AbstractNode)e).collect(Collectors.toSet());

        Set<AbstractNode> startingNodes = elements.stream()
                .filter(e -> e instanceof AbstractNode &&
                        ((AbstractNode)e).getActivationMode() == ActivationMode.STARTING_ACTION
                ).map(e -> (AbstractNode)e).collect(Collectors.toSet());

        this.activeNodes.clear();
        this.activeNodes.addAll(this.automaticNodes);
        this.activeNodes.addAll(startingNodes);

        // Reset the action points of this turn.
        this.remainedActionPoints = configs.getActionPointsPerTurn();

        this.time = 0;
        this.previousSimulatedTime = -1;
    }

    public void run(int maxTime) {
        int time = 0;
        boolean isStopped = false;

        Set<AbstractNode> automaticNodes = elements.stream()
                .filter(e -> e instanceof AbstractNode &&
                        ((AbstractNode)e).getActivationMode() == ActivationMode.AUTOMATIC
                ).map(e -> (AbstractNode)e).collect(Collectors.toSet());

        Set<AbstractNode> startingNodes = elements.stream()
                .filter(e -> e instanceof AbstractNode &&
                        ((AbstractNode)e).getActivationMode() == ActivationMode.STARTING_ACTION
                ).map(e -> (AbstractNode)e).collect(Collectors.toSet());

        Set<AbstractNode> activeNodes = new HashSet<>();
        activeNodes.addAll(automaticNodes);
        activeNodes.addAll(startingNodes);

        Set<AbstractNode> nextActiveNodes = new HashSet<>();

        while ((maxTime < 0 || time < maxTime) && !isStopped) {
            final int currentTime = time;

            // Activate each node.
            activeNodes.forEach(node -> node.activate(currentTime));

            // Clear "next" set.
            nextActiveNodes.clear();

            // Find next activate-able elements by triggers.
            Set<AbstractElement> elements = activeNodes.stream().map(node -> {
                boolean connectionsActivated =
                        node.getIncomingConnections().stream()
                                .allMatch(c -> c.getLastActivatedTime() == currentTime);

                if (connectionsActivated)
                    return node.getTriggers();
                else
                    return Collections.<Trigger>emptySet();
            }).flatMap(Set::stream).map(Trigger::getTarget).collect(Collectors.toSet());

            // Advance the time.
            time++;
        }
    }
}
