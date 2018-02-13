package com.squarebit.machinations.models;

import com.squarebit.machinations.modelsex.Resource;
import org.apache.commons.lang3.tuple.Pair;

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
     * Gets configs.
     *
     * @return the configs
     */
    public Configs getConfigs() {
        return configs;
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

    private void doSimulateOneTimeStep() {
        // STEP 1: try to resolve activation requirements
        Set<ActivationRequirement> activationRequirements = this.activeNodes.stream()
                .map(AbstractNode::getActivationRequirement)
                .collect(Collectors.toSet());

        Set<ResourceConnection> requiredConnections = activationRequirements.stream()
                .flatMap(r -> r.getConnections().stream())
                .collect(Collectors.toSet());

        Map<ResourceConnection, Integer> requiredFlows = new HashMap<>();
        requiredConnections.forEach(c -> requiredFlows.put(c, c.getFlowRate()));

        // Active connections, grouped by providing node.
        Map<AbstractNode, List<ResourceConnection>> requiredConnectionsByProvider = requiredConnections.stream()
                .collect(Collectors.groupingBy(ResourceConnection::getFrom));

        // The set of actual flow.
        Map<ResourceConnection, ResourceSet> actualFlows = new HashMap<>();

        // Get resource snapshot for each provider node.
        Map<AbstractNode, ResourceSet> resourceSnapShots = new HashMap<>();
        requiredConnectionsByProvider.forEach((n, c) -> resourceSnapShots.put(n, n.getResources().copy()));

        // Calculate the actual flow.
        activationRequirements.forEach(requirement ->
                requirement.getConnections().forEach(c -> {
                    ResourceSet snapshot = resourceSnapShots.get(c.getFrom());
                    int requiredRate = requiredFlows.get(c);
                    ResourceSet extracted = snapshot.extract(requiredRate);

                    if (extracted.size() > 0) {
                        if (requirement.isRequiringAll() && extracted.size() < requiredRate)
                            snapshot.add(extracted);
                        else
                            actualFlows.put(c, extracted);
                    }
                })
        );

        // Determines requirements actually satisfied.
        if (configs.getTimeMode() == TimeMode.SYNCHRONOUS) {
            // synchronous time mode require each provider node to provide all or none dependent connections.
            Set<ResourceConnection> nonFlows = requiredConnectionsByProvider.entrySet().stream()
                    .map(e -> {
                        List<ResourceConnection> dependentConnections = e.getValue();
                        if (actualFlows.keySet().containsAll(dependentConnections))
                            return dependentConnections;
                        else
                            return Collections.<ResourceConnection>emptyList();
                    }).flatMap(List::stream).collect(Collectors.toSet());

            // Remove those non-flow due to sync-time constraint.
            nonFlows.forEach(actualFlows::remove);
        }

        // Triggers the target nodes meeting requirements.
        Set<ActivationRequirement> satisfiedRequirements = activationRequirements.stream()
            .filter(r -> {
                Set<ResourceConnection> connections = r.getConnections();

                return (connections.isEmpty() ||
                        (r.isRequiringAll() && actualFlows.keySet().containsAll(connections)) ||
                        (!r.isRequiringAll() && connections.stream().anyMatch(actualFlows::containsKey)));
            }).collect(Collectors.toSet());

        // Activates the nodes.
        Set<ResourceConnection> firedOutgoingConnections = satisfiedRequirements.stream().map(r -> {
            Set<ResourceConnection> connections = r.getConnections();

            Map<ResourceConnection, ResourceSet> incomingFlows = new HashMap<>();
            actualFlows.entrySet().stream()
                    .filter(e -> connections.contains(e.getKey()))
                    .forEach(e -> incomingFlows.put(e.getKey(), e.getValue()));

            return r.getTarget().activate(this.time, incomingFlows);
        }).flatMap(Set::stream).collect(Collectors.toSet());

        // STEP 2: triggers.
        Set<ResourceConnection> firedConnections = firedOutgoingConnections;

        Map<AbstractNode, List<ResourceConnection>> firedConnectionByTarget =
                firedConnections.stream().collect(Collectors.groupingBy(ResourceConnection::getTo));
        Set<AbstractNode> triggerOwners = firedConnectionByTarget.entrySet().stream()
                .filter(e -> e.getKey().getIncomingConnections().containsAll(e.getValue()))
                .map(Map.Entry::getKey).collect(Collectors.toSet());

        triggerOwners.addAll(
                satisfiedRequirements.stream().map(ActivationRequirement::getTarget).collect(Collectors.toSet())
        );

        Set<AbstractElement> triggeredElements = triggerOwners.stream()
                .flatMap(o -> o.activateTriggers().stream()).map(Trigger::getTarget)
                .collect(Collectors.toSet());

        triggeredElements.forEach(e -> {
            if (e instanceof ResourceConnection) {
                ResourceConnection connection = (ResourceConnection)e;
                activateConnection(connection);
            }
            else if (e instanceof AbstractNode) {
                AbstractNode node = (AbstractNode)e;
                activateNode(node);
            }
        });

        // STEP 3: activators
        satisfiedRequirements.stream().flatMap(r -> r.getTarget().activateActivators().stream())
                .map(Activator::getTarget)
                .distinct()
                .forEach(this::activateNode);
    }

    private void activateConnection(ResourceConnection connection) {
        int rate = connection.getFlowRate();
        ResourceSet resource = connection.getFrom().getResources().extract(rate);
        connection.getTo().receive(resource);
    }

    private void activateNode(AbstractNode node) {
        ActivationRequirement requirement = node.getActivationRequirement();

        // Active connections, grouped by providing node.
        Set<AbstractNode> providers =
                requirement.getConnections().stream()
                .map(ResourceConnection::getFrom)
                .collect(Collectors.toSet());

        Map<ResourceConnection, Integer> requiredFlows = new HashMap<>();
        requirement.getConnections().forEach(c -> requiredFlows.put(c, c.getFlowRate()));

        // The set of actual flow.
        Map<ResourceConnection, ResourceSet> actualFlows = new HashMap<>();

        // Get resource snapshot for each provider node.
        Map<AbstractNode, ResourceSet> resourceSnapShots = new HashMap<>();
        providers.forEach(n -> resourceSnapShots.put(n, n.getResources().copy()));

        requirement.getConnections().forEach(c -> {
            ResourceSet snapshot = resourceSnapShots.get(c.getFrom());
            int requiredRate = requiredFlows.get(c);
            ResourceSet extracted = snapshot.extract(requiredRate);

            if (extracted.size() > 0) {
                if (requirement.isRequiringAll() && extracted.size() < requiredRate)
                    snapshot.add(extracted);
                else
                    actualFlows.put(c, extracted);
            }
        });

        Set<ResourceConnection> connections = requirement.getConnections();
        if (connections.isEmpty() ||
                (requirement.isRequiringAll() && actualFlows.keySet().containsAll(connections)) ||
                (!requirement.isRequiringAll() && connections.stream().anyMatch(actualFlows::containsKey)))
        {
            Map<ResourceConnection, ResourceSet> incomingFlows = new HashMap<>();
            actualFlows.entrySet().stream()
                    .filter(e -> connections.contains(e.getKey()))
                    .forEach(e -> incomingFlows.put(e.getKey(), e.getValue()));

            requirement.getTarget().activate(this.time, incomingFlows);
        }
    }

    /**
     *
     */
    private void __doSimulateOneTimeStep() {
        // - try pull resources
        // - activate satisfied nodes
        // -

        // Candidate connections required if any of active nodes are activated.
        Set<ResourceConnection> activeConnections = this.activeNodes.stream()
                .map(this::getActiveConnection).flatMap(Set::stream).map(e -> (ResourceConnection)e)
                .collect(Collectors.toSet());

        // Flow rate this time step.
        Map<ResourceConnection, Integer> requiredFlow = new HashMap<>();
        activeConnections.forEach(c -> requiredFlow.put(c, c.getFlowRate()));

        // Active connections, grouped by node.
        Map<AbstractNode, List<ResourceConnection>> activeConnectionsByNode = activeConnections.stream()
                .collect(Collectors.groupingBy(ResourceConnection::getFrom));

        // The set of actually satisfied connections.
        Set<ResourceConnection> satisfiedConnections = new HashSet<>();

        // The set of actual flow.
        Map<ResourceConnection, ResourceSet> actualFlows = new HashMap<>();

        //
        if (configs.getTimeMode() == TimeMode.SYNCHRONOUS) {
            activeConnectionsByNode.forEach((node, connections) -> {
                ResourceSet resourceSnapshot = node.resources.copy();
                boolean isPullingAllOrNone = node.getFlowMode() == FlowMode.PULL_ALL;

                // Calculate the actual flow.
                connections.forEach(c -> {
                    int rate = requiredFlow.get(c);
                    ResourceSet flow = resourceSnapshot.pull(c.getResourceName(), rate, isPullingAllOrNone);
                    actualFlows.put(c, flow);
                });

                // Synchronous time required all connections to be satisfied.
                boolean allSatisfied = connections.stream().allMatch(c -> actualFlows.get(c).size() > 0);
                if (allSatisfied)
                    satisfiedConnections.addAll(connections);
            });
        }
        else {
            activeConnectionsByNode.forEach((node, connections) -> {
                ResourceSet resourceSnapshot = node.resources.copy();
                boolean isPullingAllOrNone = node.getFlowMode() == FlowMode.PULL_ALL;

                // Calculate the actual flow.
                connections.forEach(c -> {
                    int rate = requiredFlow.get(c);
                    ResourceSet flow = resourceSnapshot.pull(c.getResourceName(), rate, isPullingAllOrNone);
                    actualFlows.put(c, flow);

                    if (flow.size() > 0)
                        satisfiedConnections.add(c);
                });
            });
        }

        // Activate the nodes.
        activeConnectionsByNode.forEach((node, connections) -> {
            boolean isAllOrNoneFlow = node.isAllOrNoneFlow();
            boolean shouldActivate = !isAllOrNoneFlow || satisfiedConnections.containsAll(connections);

            if (shouldActivate) {
                // Now send the resources along the satisfied connections.
                connections.forEach(c -> {
                    ResourceSet flow = actualFlows.get(c);
                    c.getFrom().removeResource(flow);
                    c.getTo().addResource(flow);
                    c.activate(this.time);
                });

                node.activate(this.time);
            }
        });

        // Modifiers

        // Triggers.

        // Activators.
    }

    private Set<ResourceConnection> getActiveConnection(AbstractNode node) {
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
