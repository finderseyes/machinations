package com.squarebit.machinations.models;

import com.google.common.collect.ImmutableSet;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.*;
import java.util.function.Supplier;

public class MachinationsContext {
    private Object specs;
    private HashSet<AbstractNode> nodes;
    private HashSet<AbstractConnection> connections;
    private HashSet<AbstractNode> startVertices;
    private HashMap<String, AbstractNode> nodeBySpec;

    /**
     * Instantiates a new Machinations context.
     */
    private MachinationsContext() {
        this.specs = null;
        this.nodes = new HashSet<>();
        this.connections = new HashSet<>();
        this.startVertices = new HashSet<>();
        this.nodeBySpec = new HashMap<>();
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
        context.specs = specs;

        List<ResourceConnectionNode> resourceConnectionNodes = new ArrayList<>();

        // Iterate all nodes.
        specs.vertices().forEachRemaining(v -> {
            try {
                AbstractNode node = context.createVertex(v);

                if (!(node instanceof ResourceConnectionNode)) {
                    context.nodes.add(node);
                    context.nodeBySpec.put(v.id().toString(), node);
                }
                else {
                    resourceConnectionNodes.add((ResourceConnectionNode)node);
                }
            }
            catch (Exception ex) {}
        });

        // Iterate all resource connection nodes.
        resourceConnectionNodes.forEach(rcn -> {
            IntSupplierFactory factory = new IntSupplierFactory();

            String rateExp = getPropertyOrDefault(rcn.getVertex(), PropertyKey.CONNECTION_RATE, "", String.class);
            try {
                Supplier<Integer> rateSupplier = factory.fromExpression(rateExp);
                ResourceConnection resourceConnection = new ResourceConnection();
                resourceConnection.setRate(rateSupplier);

                AbstractNode from = context.nodeBySpec.get(rcn.getIncomingEdge().outVertex().id().toString());
                AbstractNode to = context.nodeBySpec.get(rcn.getOutgoingEdge().inVertex().id().toString());

                from.getOutgoingConnections().add(resourceConnection);
                to.getIncomingConnections().add(resourceConnection);
                resourceConnection.setFrom(from).setTo(to);

                context.connections.add(resourceConnection);
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

        return context;
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
        else if (vertexType.equals(Constants.NODE_TYPE_RESOURCE_CONNECTION)) {
            ResourceConnectionNode rcn = new ResourceConnectionNode();
            rcn.setIncomingEdge(vertex.edges(Direction.IN).next());
            rcn.setOutgoingEdge(vertex.edges(Direction.OUT).next());
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
        IntSupplierFactory factory = new IntSupplierFactory();
        String connectionType = getPropertyOrDefault(edge, PropertyKey.CONNECTION_TYPE,
                Constants.CONNECTION_TYPE_RESOURCE_CONNECTION, String.class);

        AbstractConnection connection = null;

        if (connectionType != null && connectionType.equals(Constants.CONNECTION_TYPE_RESOURCE_CONNECTION)) {
            String outVertexType = getPropertyOrDefault(edge.outVertex(), PropertyKey.NODE_TYPE,
                Constants.NODE_TYPE_POOL, String.class);
            String inVertexType = getPropertyOrDefault(edge.inVertex(), PropertyKey.NODE_TYPE,
                    Constants.NODE_TYPE_POOL, String.class);

            if (!outVertexType.equals(Constants.NODE_TYPE_RESOURCE_CONNECTION) &&
                    !inVertexType.equals(Constants.NODE_TYPE_RESOURCE_CONNECTION)) {
                String rateExp = getPropertyOrDefault(edge, PropertyKey.CONNECTION_RATE, "", String.class);
                Supplier<Integer> rateSupplier = factory.fromExpression(rateExp);

                ResourceConnection resourceConnection = new ResourceConnection();
                resourceConnection.setRate(rateSupplier);

                connection = resourceConnection;
            }
        }

        return connection;
    }
}
