package com.squarebit.machinations.models;

import com.google.common.collect.ImmutableSet;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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

        // Iterate all nodes.
        specs.vertices().forEachRemaining(v -> {
            try {
                AbstractNode vertex = createVertex(v);
                context.nodes.add(vertex);
                context.nodeBySpec.put(v.id().toString(), vertex);
            }
            catch (Exception ex) {}
        });

        // Iterate all connections.
        specs.edges().forEachRemaining(e -> {
            try {
                AbstractConnection connection = createConnection(e);

                AbstractNode from = context.nodeBySpec.get(e.outVertex().id().toString());
                AbstractNode to = context.nodeBySpec.get(e.inVertex().id().toString());

                from.getOutgoingConnections().add(connection);
                to.getIncomingConnections().add(connection);
                connection.setFrom(from);
                connection.setTo(to);

                context.connections.add(connection);
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

    private static AbstractNode createVertex(Vertex vertex) throws Exception {
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

        if (node != null) {
            String name = getPropertyOrDefault(vertex, PropertyKey.NAME, "", String.class);

            node.setName(name);

            if (activationMode != null)
                node.setActivationMode(ActivationMode.from(activationMode));

            return node;
        }

        throw new Exception("Unknown vertex type.");
    }

    private static AbstractConnection createConnection(Edge edge) throws Exception {
        IntSupplierFactory factory = new IntSupplierFactory();
        String connectionType = getPropertyOrDefault(edge, PropertyKey.CONNECTION_TYPE,
                Constants.CONNECTION_TYPE_RESOURCE_CONNECTION, String.class);

        AbstractConnection connection = null;

        if (connectionType != null && connectionType.equals(Constants.CONNECTION_TYPE_RESOURCE_CONNECTION)) {
            String rateExp = getPropertyOrDefault(edge, PropertyKey.RATE, "", String.class);
            Supplier<Integer> rateSupplier = factory.fromExpression(rateExp);

            ResourceConnection resourceConnection = new ResourceConnection();
            resourceConnection.setRate(rateSupplier);

            connection = resourceConnection;
        }

        return connection;
    }
}
