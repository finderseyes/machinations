package com.squarebit.machinations.models;

import com.squarebit.machinations.parsers.DiceLexer;
import com.squarebit.machinations.parsers.DiceParser;
import com.squarebit.machinations.specs.yaml.ElementSpec;
import com.squarebit.machinations.specs.yaml.NodeSpec;
import com.squarebit.machinations.specs.yaml.PoolSpec;
import com.squarebit.machinations.specs.yaml.YamlSpec;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class MachinationsContextFactory {
    /**
     *
     */
    private class BuildingContext {
        private MachinationsContext machinations;
        private YamlSpec spec;
        private Map<AbstractElement, ElementSpec> elementSpec = new HashMap<>();
    }

    /**
     *
     */
    private class ConnectionBuildContext {
        private AbstractNode from;
        private AbstractNode to;
        private String label = "";
        private String id;

        public AbstractNode getFrom() {
            return from;
        }

        public ConnectionBuildContext setFrom(AbstractNode from) {
            this.from = from;
            return this;
        }

        public AbstractNode getTo() {
            return to;
        }

        public ConnectionBuildContext setTo(AbstractNode to) {
            this.to = to;
            return this;
        }

        public String getLabel() {
            return label;
        }

        public ConnectionBuildContext setLabel(String label) {
            this.label = label;
            return this;
        }

        public String getId() {
            return id;
        }

        public ConnectionBuildContext setId(String id) {
            this.id = id;
            return this;
        }
    }

    public MachinationsContext fromSpec(YamlSpec spec) throws Exception {
        BuildingContext context = new BuildingContext();
        context.machinations = new MachinationsContext();
        context.spec = spec;

        this.createNode(context, spec);
        this.createImplicitConnections(context);
        this.createExplicitConnections(context, spec);

        return context.machinations;
    }

    private void createNode(BuildingContext context, YamlSpec spec) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        // Build the node, first pass.
        spec.getNodes().forEach(nodeSpec -> {
            if (lastError.get() != null)
                return;

            String id = getOrCreateId(nodeSpec.getId());
            AbstractNode node = null;

            if (nodeSpec instanceof PoolSpec) {
                PoolSpec poolSpec = (PoolSpec)nodeSpec;
                Pool pool = new Pool();
                buildResourcesDecl(pool, poolSpec.getResources());
                buildCapacityDecl(pool, poolSpec.getCapacity());
                node = pool;
            }

            if (node != null) {
                try {
                    node.machinations = context.machinations;
                    node.setName(nodeSpec.getName())
                            .setActivationMode(ActivationMode.from(nodeSpec.getActivationMode()))
                            .setFlowMode(FlowMode.from(nodeSpec.getFlowMode()))
                            .setId(id);

                    context.machinations.addElement(node);
                    context.elementSpec.put(node, nodeSpec);
                }
                catch (Exception ex) {
                    lastError.compareAndSet(null, ex);
                }
            }
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createExplicitConnections(BuildingContext context, YamlSpec spec) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        spec.getConnections().forEach(c -> {
            try {
                ConnectionBuildContext connectionBuildContext = getConnectionBuildContext(context, c);
                if (connectionBuildContext.from == null)
                    throw new Exception(String.format("Source node identifier is required for connection %s", c));

                createConnection(context, connectionBuildContext);
            }
            catch (Exception ex) {
                lastError.compareAndSet(null, ex);
            }
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createImplicitConnections(BuildingContext context) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        List<AbstractNode> nodes = context.machinations.getElements().stream()
                .filter(e -> e instanceof AbstractNode).map(e -> (AbstractNode)e).collect(Collectors.toList());

        nodes.forEach(node -> {
            NodeSpec spec = (NodeSpec)context.elementSpec.get(node);
            spec.getConnections().forEach(c -> {
                try {
                    ConnectionBuildContext connectionBuildContext = getConnectionBuildContext(context, c);
                    connectionBuildContext.from = node;
                    createConnection(context, connectionBuildContext);
                }
                catch (Exception ex) {
                    lastError.compareAndSet(null, ex);
                }
            });
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createConnection(BuildingContext context, ConnectionBuildContext connectionBuildContext) throws Exception {
        ResourceConnection connection = new ResourceConnection();

        connection.setFrom(connectionBuildContext.from)
                .setTo(connectionBuildContext.to)
                .setLabel(connectionBuildContext.label)
                .setId(getOrCreateId(connectionBuildContext.id));

        context.machinations.addElement(connection);
        connectionBuildContext.from.getOutgoingConnections().add(connection);
        connectionBuildContext.to.getIncomingConnections().add(connection);
    }

    private ConnectionBuildContext getConnectionBuildContext(BuildingContext context, String connection) throws Exception {
        DiceParser parser = getParser(connection);
        ConnectionBuildContext buildContext = new ConnectionBuildContext();

        ParseTree decl = parser.connectionLabel().children.get(0);
        int next = 0;
        ParseTree nextDecl = decl.getChild(next);

        if (decl instanceof DiceParser.ExplicitConnectionLabelContext) {
            buildContext.from = (AbstractNode)context.machinations.findById(nextDecl.getText());
            if (buildContext.from == null)
                throw new Exception(String.format("Unknown identifier %s", nextDecl.getText()));

            next += 2;
        }

        nextDecl = decl.getChild(next);
        if (nextDecl instanceof DiceParser.ExpressionContext) {
            buildContext.label = nextDecl.getText();
            next += 2;
        }
        else if (((TerminalNode)nextDecl).getSymbol().getType() == DiceParser.TO)
            next += 1;

        nextDecl = decl.getChild(next);
        buildContext.to = (AbstractNode)context.machinations.findById(nextDecl.getText());
        if (buildContext.to == null)
            throw new Exception(String.format("Unknown identifier %s", nextDecl.getText()));

        next += 2;
        nextDecl = decl.getChild(next);
        if (nextDecl != null) {
            buildContext.id =  nextDecl.getText();
        }

        return buildContext;
    }

    private DiceParser getParser(String expression) {
        CharStream stream = new ANTLRInputStream(expression);
        TokenStream tokens = new CommonTokenStream(new DiceLexer(stream));

        DiceParser parser = new DiceParser(tokens);
        return parser;
    }

    private void buildResourcesDecl(AbstractNode node, String resourceExpression) {
        Map<String, Integer> nodeResources = node.getResources();
        nodeResources.clear();

        if (resourceExpression == null || resourceExpression.trim().equals(""))
            return;

        DiceParser parser = getParser(resourceExpression);
        DiceParser.ResourceExpressionContext context = parser.resourceExpression();
        context.children.forEach(c -> {
            if (c instanceof DiceParser.SingleResourceExpressionContext) {
                DiceParser.SingleResourceExpressionContext decl = (DiceParser.SingleResourceExpressionContext)c;

                int count = Integer.parseInt(decl.INT().getText());
                String name = decl.IDENTIFIER() != null ? decl.IDENTIFIER().getText().trim() :
                        MachinationsContext.DEFAULT_RESOURCE_NAME;

                if (nodeResources.putIfAbsent(name, count) != null)
                    nodeResources.compute(name, (n, c0) -> (c0 + count));
            }
        });
    }

    private void buildCapacityDecl(AbstractNode node, String resourceExpression) {
        Map<String, Integer> capacity = node.getCapacity();
        capacity.clear();

        if (resourceExpression == null || resourceExpression.trim().equals(""))
            return;

        DiceParser parser = getParser(resourceExpression);
        DiceParser.ResourceExpressionContext context = parser.resourceExpression();
        context.children.forEach(c -> {
            if (c instanceof DiceParser.SingleResourceExpressionContext) {
                DiceParser.SingleResourceExpressionContext decl = (DiceParser.SingleResourceExpressionContext)c;

                int count = Integer.parseInt(decl.INT().getText());
            String name = decl.IDENTIFIER() != null ? decl.IDENTIFIER().getText().trim() :
                        MachinationsContext.DEFAULT_RESOURCE_NAME;

                if (capacity.putIfAbsent(name, count) != null)
                    capacity.compute(name, (n, c0) -> (c0 + count));
            }
        });
    }

    private String getOrCreateId(String id) {
        if (id != null && !id.equals(""))
            return id;
        else
            return ObjectId.get().toHexString();
    }
}
