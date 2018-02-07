package com.squarebit.machinations.models;

import com.google.common.collect.ImmutableSet;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.yaml.snakeyaml.scanner.Constant;

import java.util.*;
import java.util.function.Supplier;

public class MachinationsContext {
    private Object specs;
    private HashSet<AbstractNode> nodes;
    private HashSet<AbstractConnection> connections;
    private HashSet<AbstractNode> startVertices;
    private HashMap<String, AbstractNode> nodeBySpec;
    private IntSupplierFactory expressionFactory;

    /**
     * Instantiates a new Machinations context.
     */
    private MachinationsContext() {
        this.specs = null;
        this.nodes = new HashSet<>();
        this.connections = new HashSet<>();
        this.startVertices = new HashSet<>();
        this.nodeBySpec = new HashMap<>();
        this.expressionFactory = new IntSupplierFactory();
    }

    public Set<AbstractNode> getNodes() {
        return ImmutableSet.copyOf(this.nodes);
    }

    public Set<AbstractConnection> getConnections() {
        return this.connections;
    }

    public Optional<AbstractNode> getNode(String key) {
        if (this.nodeBySpec.containsKey(key))
            return Optional.of(this.nodeBySpec.get(key));
        else
            return Optional.empty();
    }

    public static MachinationsContext fromSpecs(TinkerGraph specs) {
        MachinationsContext context = new MachinationsContext();
        List<ConnectionLabel> connectionLabels = new ArrayList<>();
        GraphTraversalSource traversal = specs.traversal();
        HashMap<Vertex, AbstractConnection> connectionByLabel = new HashMap<>();

        context.specs = specs;

        // Iterate all nodes.
        specs.vertices().forEachRemaining(v -> {
            try {
                AbstractNode node = context.createVertex(v);

                if (!(node instanceof ConnectionLabel)) {
                    context.nodes.add(node);
                    context.nodeBySpec.put(v.id().toString(), node);
                }
                else {
                    connectionLabels.add((ConnectionLabel)node);
                }
            }
            catch (Exception ex) {}
        });

        // Iterate all connection label nodes.
        connectionLabels.forEach(rcn -> {
            String rateExp = getPropertyOrDefault(rcn.getVertex(), PropertyKey.CONNECTION_RATE, "", String.class);
            try {
                Supplier<Integer> rateSupplier = context.expressionFactory.fromExpression(rateExp);
                ResourceConnection resourceConnection = new ResourceConnection();
                resourceConnection.setRate(rateSupplier);

                AbstractNode from = context.nodeBySpec.get(rcn.getIncomingEdge().outVertex().id().toString());
                AbstractNode to = context.nodeBySpec.get(rcn.getOutgoingEdge().inVertex().id().toString());

                from.getOutgoingConnections().add(resourceConnection);
                to.getIncomingConnections().add(resourceConnection);
                resourceConnection.setFrom(from).setTo(to);

                context.connections.add(resourceConnection);
                connectionByLabel.put(rcn.getVertex(), resourceConnection);
            }
            catch (Exception ex) {}
        });

        // Iterate all other connections.
        specs.edges().forEachRemaining(e -> {
            try {
                AbstractNode from = context.nodeBySpec.get(e.outVertex().id().toString());
                AbstractNode to = context.nodeBySpec.get(e.inVertex().id().toString());

                AbstractConnection connection = context.createConnection(e);

                if (connection != null) {
                    from.getOutgoingConnections().add(connection);
                    to.getIncomingConnections().add(connection);
                    connection.setFrom(from);
                    connection.setTo(to);

                    context.connections.add(connection);
                }
            }
            catch (Exception ex) {}
        });

        // Modifiers
        traversal.E().filter(t ->
                elementHasProperty(t.get(), PropertyKey.CONNECTION_TYPE, Constants.CONNECTION_TYPE_MOFIFIER)
        ).forEachRemaining(e -> {
            AbstractNode owner = context.nodeBySpec.get(e.outVertex().id().toString());

            if (elementHasProperty(e.inVertex(), PropertyKey.NODE_TYPE, Constants.NODE_TYPE_CONNECTION_LABEL)) {
                AbstractConnection target = connectionByLabel.get(e.inVertex());
                Modifier modifier = new Modifier().setOwner(owner).setTarget(target);

                owner.getModifiers().add(modifier);
                target.getModifyingElements().add(modifier);
            }
            else {
                AbstractNode target = context.nodeBySpec.get(e.inVertex().id().toString());
                Modifier modifier = new Modifier().setOwner(owner).setTarget(target);

                owner.getModifiers().add(modifier);
                target.getModifyingElements().add(modifier);
            }
        });

        return context;
    }

    private static boolean elementHasProperty(Element element, String key, String value) {
        String v = getPropertyOrDefault(element, key, "", String.class);
        return v.equals(value);
    }

    private static <T> T getPropertyOrDefault(Element element, String key, T defaultValue, Class<T> valueType) {
        if (element.keys().contains(key)) {
            Object value = element.property(key).value();

            if (value != null && valueType.isInstance(value))
                return valueType.cast(value);
            else
                return defaultValue;
        }
        else
            return defaultValue;
    }

    private static String getPropertyOrDefault(Element element, String key, String defaultValue) {
        return getPropertyOrDefault(element, key, defaultValue, String.class);
    }

    private AbstractNode createVertex(Vertex vertex) throws Exception {
        String vertexType = getPropertyOrDefault(vertex, PropertyKey.NODE_TYPE, Constants.NODE_TYPE_POOL, String.class);
        String activationMode =
                getPropertyOrDefault(vertex, PropertyKey.ACTIVATION_MODE, Constants.ACTIVATION_MODE_PASSIVE, String.class);

        AbstractNode node = null;

        if (vertexType != null && vertexType.equals(Constants.NODE_TYPE_POOL)) {
            int initialSize = getPropertyOrDefault(vertex, PropertyKey.POOL_SIZE, 0, Integer.class);

            Pool pool = new Pool();
            pool.setInitialSize(initialSize);
            node = pool;
        }
        else if (vertexType.equals(Constants.NODE_TYPE_CONNECTION_LABEL)) {
            GraphTraversalSource traversalSource = vertex.graph().traversal();

            List<Edge> outEdges = traversalSource.V(vertex.id()).outE().filter(t -> {
                String connectionType = getPropertyOrDefault(t.get(), PropertyKey.CONNECTION_TYPE,
                        Constants.CONNECTION_TYPE_RESOURCE_CONNECTION, String.class);
                return connectionType.equals(Constants.CONNECTION_TYPE_RESOURCE_CONNECTION);
            }).toList();

            List<Edge> inEdges = traversalSource.V(vertex.id()).inE().filter(t -> {
                String connectionType = getPropertyOrDefault(t.get(), PropertyKey.CONNECTION_TYPE,
                        Constants.CONNECTION_TYPE_RESOURCE_CONNECTION, String.class);
                return connectionType.equals(Constants.CONNECTION_TYPE_RESOURCE_CONNECTION);
            }).toList();

            if (outEdges.size() != 1 || inEdges.size() != 1)
                throw new Exception("Connection label node requires exactly one incoming and one out going resource connection.");

            ConnectionLabel rcn = new ConnectionLabel();
            rcn.setIncomingEdge(inEdges.get(0));
            rcn.setOutgoingEdge(outEdges.get(0));
            rcn.setVertex(vertex);
            node = rcn;
        }

        if (node != null) {
            String name = getPropertyOrDefault(vertex, PropertyKey.NAME, "", String.class);

            node.setName(name);

            if (activationMode != null)
                node.setActivationMode(ActivationMode.from(activationMode));

            return node;
        }

        throw new Exception("Unknown vertex type.");
    }

    private AbstractConnection createConnection(Edge edge) throws Exception {
        String connectionType = getPropertyOrDefault(edge, PropertyKey.CONNECTION_TYPE,
                Constants.CONNECTION_TYPE_RESOURCE_CONNECTION, String.class);

        AbstractConnection connection = null;

        if (connectionType != null && connectionType.equals(Constants.CONNECTION_TYPE_RESOURCE_CONNECTION)) {
            String outVertexType = getPropertyOrDefault(edge.outVertex(), PropertyKey.NODE_TYPE,
                Constants.NODE_TYPE_POOL, String.class);
            String inVertexType = getPropertyOrDefault(edge.inVertex(), PropertyKey.NODE_TYPE,
                    Constants.NODE_TYPE_POOL, String.class);

            if (!outVertexType.equals(Constants.NODE_TYPE_CONNECTION_LABEL) &&
                    !inVertexType.equals(Constants.NODE_TYPE_CONNECTION_LABEL)) {
                String rateExp = getPropertyOrDefault(edge, PropertyKey.CONNECTION_RATE, "", String.class);
                Supplier<Integer> rateSupplier = this.expressionFactory.fromExpression(rateExp);

                ResourceConnection resourceConnection = new ResourceConnection();
                resourceConnection.setRate(rateSupplier);

                connection = resourceConnection;
            }
        }

        return connection;
    }
}
