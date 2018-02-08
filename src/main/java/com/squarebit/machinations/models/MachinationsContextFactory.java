package com.squarebit.machinations.models;

import com.squarebit.machinations.parsers.DiceLexer;
import com.squarebit.machinations.parsers.DiceParser;
import com.squarebit.machinations.specs.yaml.ElementSpec;
import com.squarebit.machinations.specs.yaml.PoolSpec;
import com.squarebit.machinations.specs.yaml.YamlSpec;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MachinationsContextFactory {
    private class BuildingContext {
        private MachinationsContext machinations;
        private YamlSpec spec;
        private Map<AbstractElement, ElementSpec> elementSpec = new HashMap<>();
    }

    public MachinationsContext fromSpec(YamlSpec spec) throws Exception {
        BuildingContext context = new BuildingContext();
        context.machinations = new MachinationsContext();
        context.spec = spec;

        this.createNode(context, spec);
        this.createNodeConnections(context);

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

    private void createNodeConnections(BuildingContext context) {

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
                String name = decl.RESOURCE_NAME() != null ? decl.RESOURCE_NAME().getText().trim() :
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
                String name = decl.RESOURCE_NAME() != null ? decl.RESOURCE_NAME().getText().trim() :
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
