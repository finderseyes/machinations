package com.squarebit.machinations.models;

import java.util.*;
import java.util.stream.Collectors;

public class Machinations {
    public static final String DEFAULT_RESOURCE_NAME = "";

    private Configs configs = new Configs();
    private Set<Element> elements;
    private Map<String, Element> elementById;

    private int previousSimulatedTime = -1;
    private int time = -1;  // The time index now, negative means no simulation step has been executed.
    private int remainedActionPoints = -1;
    private Set<Node> activeNodes = new HashSet<>();
    private Set<Node> automaticNodes;
    private Set<Node> activatedByActivatorNodes = new HashSet<>();
    private boolean terminated = false;

    public Machinations() {
        this.elements = new HashSet<>();
        this.elementById = new HashMap<>();
    }

    /**
     * Gets configs.
     *
     * @return the configs
     */
    public Configs getConfigs() {
        return configs;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * Is terminated boolean.
     *
     * @return the boolean
     */
    public boolean isTerminated() {
        return terminated;
    }

    /**
     * Find by id abstract element.
     *
     * @param id the id
     * @return the abstract element
     */
    public Element findById(String id) {
        return this.elementById.get(id);
    }

    /**
     * Gets elements.
     *
     * @return the elements
     */
    public Set<Element> getElements() {
        return elements;
    }

    /**
     * AdditionIntegerExpression element.
     *
     * @param element the element
     * @throws Exception the exception
     */
    protected void addElement(Element element) throws Exception {
        if (!this.elementById.containsKey(element.getId())) {
            this.elements.add(element);
            this.elementById.put(element.getId(), element);
            element.machinations = this;
        }
        else {
            throw new Exception(String.format("An element with id %s is already existed.", element.getId()));
        }
    }

    /**
     * Performs one simulation step.
     */
    public boolean simulateOneTimeStep() {
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

        return (!this.terminated);
    }

    private void doSimulateOneTimeStep() {
        // 1. fire the active elements, and find out which ones needs to be fired.
        // 2. try to satisfy fire requests.
        // 3. fire those satisfied.
        // 4. repeat until no more active elements in this time step.

        if (this.terminated)
            return;

        Set<Node> activeNodes = getActiveNodes();
        this.activatedByActivatorNodes.clear();

        // The initial fired nodes.
        Set<Node> nodes = activeNodes.stream()
                .filter(Node::isEnabled)
                .filter(Node::activate)
                .collect(Collectors.toSet());

//        // Stop if there is an end node.
//        if (nodes.stream().anyMatch(n -> n instanceof End)) {
//            this.terminated = true;
//            return;
//        }

        // Elements already fired in this time step.
        Set<GraphElement> firedElements = new HashSet<>(nodes);

        Set<GraphElement> nextElements = this.fireNodes(nodes);
        nextElements.removeAll(firedElements);

        while (!nextElements.isEmpty()) {
            firedElements.addAll(nextElements);

            // Fire the next connections
            Set<ResourceConnection> connections = nextElements.stream()
                    .filter(e -> e instanceof ResourceConnection).map(e -> (ResourceConnection)e)
                    .collect(Collectors.toSet());
            fireResourceConnections(connections);

            // Fire the next nodes
            nodes = nextElements.stream()
                    .filter(e -> e instanceof Node).map(e -> (Node)e)
                    .collect(Collectors.toSet());

            nextElements = fireNodes(nodes);
            nextElements.removeAll(firedElements);
        }
    }

    private void fireResourceConnections(Set<ResourceConnection> connections) {
        connections.forEach(c -> {
            ResourceSet requiredResource = c.fire();
            ResourceSet extracted = c.getFrom().extract(requiredResource);

            if (extracted.size() > 0) {
                c.getTo().receive(extracted);
            }
        });
    }

    /**
     * Fires a set of nodes and return the set of graph elements to be fired next.
     * @param nodes the node to be fired
     * @return the graph elements to be fired next
     */
    private Set<GraphElement> fireNodes(Set<Node> nodes) {
        Set<FireRequirement> fireRequirements = nodes.stream()
                .map(Node::getFireRequirement)
                .collect(Collectors.toSet());

        // Try to resolve requirements.
        Map<ResourceConnection, ResourceSet> flowables = resolveFireRequirements(fireRequirements);

        // The connections actually fired.
        Set<ResourceConnection> firedConnections = new HashSet<>();

        // Filter those requirements actually satisfied
        Set<FireRequirement> satisfiedRequirements = fireRequirements.stream()
                .filter(r -> {
                    Set<ResourceConnection> connections = r.getConnections();

                    return (connections.isEmpty() ||
                            (r.isRequiringAll() && flowables.keySet().containsAll(connections)) ||
                            (!r.isRequiringAll() && connections.stream().anyMatch(flowables::containsKey)));
                }).collect(Collectors.toSet());

        // Fire the nodes.
        Set<ResourceConnection> firedOutgoingConnections = satisfiedRequirements.stream().map(r -> {
            Set<ResourceConnection> connections = r.getConnections();

            ResourceSet incomingResources = new ResourceSet();

            connections.forEach(c -> {
                if (flowables.containsKey(c)) {
                    firedConnections.add(c);

                    ResourceSet requiredResources = flowables.get(c);
                    ResourceSet extracted = c.getFrom().extract(requiredResources);
                    incomingResources.add(extracted);
                }
            });

            if (r.getTarget() instanceof End)
                this.terminated = true;

            return r.getTarget().fire(incomingResources);
        }).flatMap(Set::stream).collect(Collectors.toSet());

        ///////////////////////
        // --> Triggers.

        // Add those fired out-going connections to incoming fired ones.
        firedConnections.addAll(firedOutgoingConnections);

        Map<Node, List<ResourceConnection>> firedConnectionByTarget =
                firedConnections.stream().collect(Collectors.groupingBy(ResourceConnection::getTo));

        // Trigger owners are those having all input connections satisfied
        Set<Node> triggerOwners = firedConnectionByTarget.entrySet().stream()
                .filter(e -> e.getKey().getIncomingConnections().containsAll(e.getValue()))
                .map(Map.Entry::getKey).collect(Collectors.toSet());

        // Or those nodes previously fired but do not have any input requirements.
        triggerOwners.addAll(
                satisfiedRequirements.stream()
                        .filter(r -> r.getTarget().getIncomingConnections().size() == 0)
                        .map(FireRequirement::getTarget)
                        .collect(Collectors.toSet())
        );

        // Elements will be fired are target of triggers.
        Set<GraphElement> firedNextElements = triggerOwners.stream()
                .flatMap(o -> o.activateTriggers().stream()).map(Trigger::getTarget)
                .collect(Collectors.toSet());

        //////////////////////////
        // -->  Reverse triggers
        Set<FireRequirement> unsatisfiedRequirements =
                fireRequirements.stream().filter(r -> !satisfiedRequirements.contains(r)).collect(Collectors.toSet());

        unsatisfiedRequirements.forEach(
                r -> r.getTarget().getTriggers().stream().filter(Trigger::isReverse)
                        .forEach(t -> firedNextElements.add(t.getTarget()))
        );

        /////////////////
        // --> Activators
        // NOTE: nodes activated by activators will be activated in the next time step, unless it's also triggered
        // by one trigger, hence be extra mindful when using end condition. For example, in flow-19.yaml when
        // the simulation ends, p3 must have value 12, not 8.
        //
        Map<Node, Boolean> nodeActivation = getNodeActivation();
        nodeActivation.forEach((node, isActivated) -> {
            node.setEnabled(isActivated);
            if (isActivated)
                this.activatedByActivatorNodes.add(node);
        });

        return firedNextElements;
    }

    /**
     * Gets node activation.
     * @return node activation state
     */
    private Map<Node, Boolean> getNodeActivation() {
        Map<Node, Boolean> results = new HashMap<>();

        Map<Node, List<Activator>> activatorsByTarget = elements.stream()
                .filter(e -> e instanceof Node).map(e -> (Node)e)
                .flatMap(n -> n.getActivators().stream())
                .collect(Collectors.groupingBy(Activator::getTarget));

        activatorsByTarget.forEach((node, activators) -> {
            boolean isActivated = activators.stream()
                    .map(Activator::evaluate).reduce(true, (a, b) -> a && b);

            results.putIfAbsent(node, isActivated);
        });

        return results;
    }

    /**
     * Tries to resolves give node fire requirements and return the actual flows.
     *
     * @param fireRequirements the requirement set
     * @return the set of resources actually need to pass through the connections.
     */
    private Map<ResourceConnection, ResourceSet> resolveFireRequirements(Set<FireRequirement> fireRequirements) {
        // The required connections to be fired.
        Set<ResourceConnection> requiredConnections = fireRequirements.stream()
                .flatMap(r -> r.getConnections().stream())
                .collect(Collectors.toSet());

        // Active connections, grouped by providing node.
        Map<Node, List<ResourceConnection>> requiredConnectionsByProvider = requiredConnections.stream()
                .collect(Collectors.groupingBy(ResourceConnection::getFrom));

        // The required resources for each node.
        Map<ResourceConnection, ResourceSet> requiredResources = new HashMap<>();
        requiredConnections.forEach(c -> requiredResources.put(c, c.fire()));

        // The set of actual flow.
        Map<ResourceConnection, ResourceSet> flowables = new HashMap<>();

        // Get resource snapshot for each provider node.
        Map<Node, ResourceSet> resourceSnapShots = new HashMap<>();
        requiredConnectionsByProvider.forEach((n, c) -> resourceSnapShots.put(n, n.getResources().copy()));

        // Calculate the actual flow.
        fireRequirements.forEach(requirement ->
                requirement.getConnections().forEach(c -> {
                    ResourceSet snapshot = resourceSnapShots.get(c.getFrom());
                    ResourceSet requiredResource = requiredResources.get(c);
                    ResourceSet extracted = snapshot.remove(requiredResource);

                    if (extracted.size() > 0) {
                        if (requirement.isRequiringAll() && !requiredResource.isSubSetOf(extracted))
                            snapshot.add(extracted);
                        else
                            flowables.put(c, extracted);
                    }
                })
        );

        // Determines requirements actually satisfied.
        if (configs.getTimeMode() == TimeMode.SYNCHRONOUS) {
            // synchronous time mode require each provider node to provide all or none dependent connections.
            Set<ResourceConnection> nonFlows = requiredConnectionsByProvider.entrySet().stream()
                    .map(e -> {
                        List<ResourceConnection> dependentConnections = e.getValue();
                        if (!flowables.keySet().containsAll(dependentConnections))
                            return dependentConnections;
                        else
                            return Collections.<ResourceConnection>emptyList();
                    }).flatMap(List::stream).collect(Collectors.toSet());

            // Remove those non-flow due to sync-time constraint.
            nonFlows.forEach(flowables::remove);
        }

        return flowables;
    }

    /**
     * Gets the set of nodes to be activated at current time step.
     * @return the active node set
     */
    private Set<Node> getActiveNodes() {
        Set<Node> nodes = new HashSet<>(this.automaticNodes);

        if (time == 0)
            nodes.addAll(elements.stream()
                    .filter(e -> e instanceof Node &&
                            ((Node)e).getActivationMode() == ActivationMode.STARTING_ACTION
                    ).map(e -> (Node)e).collect(Collectors.toSet()));

        nodes.addAll(this.activatedByActivatorNodes);

        return nodes;
    }

    /**
     * Initializes the context if needed before simulation.
     */
    protected void initializeIfNeeded() {
        // already simulated at least one step, no need to initialize.
        if (this.time >= 0)
            return;

        this.automaticNodes = elements.stream()
                .filter(e -> e instanceof Node && (((Node)e).getActivationMode() == ActivationMode.AUTOMATIC)
                ).map(e -> (Node)e).collect(Collectors.toSet());

        Set<Node> startingNodes = elements.stream()
                .filter(e -> e instanceof Node &&
                        ((Node)e).getActivationMode() == ActivationMode.STARTING_ACTION
                ).map(e -> (Node)e).collect(Collectors.toSet());

        this.activeNodes.clear();
        this.activeNodes.addAll(this.automaticNodes);
        this.activeNodes.addAll(startingNodes);

//        this.updateNodeEnablingStates();

        getNodeActivation().forEach(Node::setEnabled);

        // Reset the action points of this turn.
        this.remainedActionPoints = configs.getActionPointsPerTurn();

        this.time = 0;
        this.previousSimulatedTime = -1;
    }

    public void run(int maxTime) {
        int time = 0;
        boolean isStopped = false;

        Set<Node> automaticNodes = elements.stream()
                .filter(e -> e instanceof Node &&
                        ((Node)e).getActivationMode() == ActivationMode.AUTOMATIC
                ).map(e -> (Node)e).collect(Collectors.toSet());

        Set<Node> startingNodes = elements.stream()
                .filter(e -> e instanceof Node &&
                        ((Node)e).getActivationMode() == ActivationMode.STARTING_ACTION
                ).map(e -> (Node)e).collect(Collectors.toSet());

        Set<Node> activeNodes = new HashSet<>();
        activeNodes.addAll(automaticNodes);
        activeNodes.addAll(startingNodes);

        Set<Node> nextActiveNodes = new HashSet<>();

        while ((maxTime < 0 || time < maxTime) && !isStopped) {
            final int currentTime = time;

            // Activate each node.
            // activeNodes.forEach(node -> node.fire(currentTime));

            // Clear "next" set.
            nextActiveNodes.clear();

//            // Find next fire-able elements by triggers.
//            Set<Element> elements = activeNodes.stream().map(node -> {
//                boolean connectionsActivated =
//                        node.getIncomingConnections().stream()
//                                .allMatch(c -> c.getLastActivatedTime() == currentTime);
//
//                if (connectionsActivated)
//                    return node.getTriggers();
//                else
//                    return Collections.<Trigger>emptySet();
//            }).flatMap(Set::stream).map(Trigger::getTarget).collect(Collectors.toSet());

            // Advance the time.
            time++;
        }
    }
}
