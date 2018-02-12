package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.*;
import com.squarebit.machinations.parsers.DiceLexer;
import com.squarebit.machinations.parsers.DiceParser;
import com.squarebit.machinations.specs.yaml.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.antlr.v4.runtime.*;
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
        private Map<Object, Object> buildContext = new HashMap<>();
    }

    /**
     *
     */
    private class ConnectionBuildContext {
        private AbstractNode from;
        private AbstractNode to;
        private DiceParser.ExpressionContext labelExpression;
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

        public DiceParser.ExpressionContext getLabelExpression() {
            return labelExpression;
        }

        public ConnectionBuildContext setLabelExpression(DiceParser.ExpressionContext labelExpression) {
            this.labelExpression = labelExpression;
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

    private class ModifierBuildContext {
        private AbstractNode owner;
        private AbstractElement target;
        private DiceParser.ArithmeticExpressionContext expression;

        public AbstractNode getOwner() {
            return owner;
        }

        public ModifierBuildContext setOwner(AbstractNode owner) {
            this.owner = owner;
            return this;
        }

        public AbstractElement getTarget() {
            return target;
        }

        public ModifierBuildContext setTarget(AbstractElement target) {
            this.target = target;
            return this;
        }

        public DiceParser.ArithmeticExpressionContext getExpression() {
            return expression;
        }

        public ModifierBuildContext setExpression(DiceParser.ArithmeticExpressionContext expression) {
            this.expression = expression;
            return this;
        }
    }

    private class TriggerBuildContext {
        private AbstractNode owner;
        private AbstractElement target;

        public AbstractNode getOwner() {
            return owner;
        }

        public TriggerBuildContext setOwner(AbstractNode owner) {
            this.owner = owner;
            return this;
        }

        public AbstractElement getTarget() {
            return target;
        }

        public TriggerBuildContext setTarget(AbstractElement target) {
            this.target = target;
            return this;
        }
    }

    private class ActivatorBuildContext {
        private AbstractNode owner;
        private AbstractNode target;
        private DiceParser.LogicalExpressionContext condition;

        public AbstractNode getOwner() {
            return owner;
        }

        public ActivatorBuildContext setOwner(AbstractNode owner) {
            this.owner = owner;
            return this;
        }

        public AbstractNode getTarget() {
            return target;
        }

        public ActivatorBuildContext setTarget(AbstractNode target) {
            this.target = target;
            return this;
        }

        public DiceParser.LogicalExpressionContext getCondition() {
            return condition;
        }

        public ActivatorBuildContext setCondition(DiceParser.LogicalExpressionContext condition) {
            this.condition = condition;
            return this;
        }
    }

    /**
     * From spec machinations context.
     *
     * @param spec the spec
     * @return the machinations context
     * @throws Exception the exception
     */
    public MachinationsContext fromSpec(YamlSpec spec) throws Exception {
        BuildingContext context = new BuildingContext();
        context.machinations = new MachinationsContext();
        context.spec = spec;

        this.createNode(context, spec);

        // Connections.
        this.createImplicitConnections(context);
        this.createExplicitConnections(context, spec);

        // Modifiers.
        this.createImplicitModifiers(context);
        this.createExplicitModifiers(context, spec);

        // Trigger
        this.createImplicitTrigger(context);
        this.createExplicitTrigger(context, spec);

        // Activator.
        this.createImplicitActivators(context);
        this.createExplicitActivators(context, spec);

        // --> Third pass.

        // Build connection flow expression.
        context.machinations.getElements().stream()
                .filter(e -> e instanceof ResourceConnection).map(e -> (ResourceConnection)e)
                .forEach(connection -> {
                    ConnectionBuildContext buildContext =
                            (ConnectionBuildContext)context.buildContext.get(connection);
                    if (buildContext.labelExpression != null) {
                        connection.setFlowRateExpression(buildExpression(context, buildContext.labelExpression));
                    }
                });

        // Modifiers, triggers and activators.
        context.machinations.getElements().stream()
                .filter(e -> e instanceof AbstractNode).map(e -> (AbstractNode)e)
                .forEach(node -> {
                    node.getModifiers().forEach(modifier -> {
                        ModifierBuildContext buildContext =
                                (ModifierBuildContext)context.buildContext.get(modifier);

                        if (buildContext.expression != null) {
                            modifier.setRateExpression(buildArithmetic(context, buildContext.expression));
                        }
                    });

                    node.getActivators().forEach(activator -> {
                        ActivatorBuildContext buildContext =
                                (ActivatorBuildContext)context.buildContext.get(activator);

                        if (buildContext.condition != null) {
                            activator.setConditionExpression(buildBoolean(context, buildContext.condition));
                        }
                    });
                });

        return context.machinations;
    }

    public Expression buildExpression(BuildingContext context,
                                      DiceParser.ExpressionContext expressionContext)
    {
        ParseTree decl = expressionContext.getChild(0);

        if (decl instanceof DiceParser.ArithmeticExpressionContext)
            return buildArithmetic(context, (DiceParser.ArithmeticExpressionContext)decl);
        else if (decl instanceof DiceParser.LogicalExpressionContext)
            return buildBoolean(context, (DiceParser.LogicalExpressionContext)decl);

        return null;
    }

    public BooleanExpression buildBoolean(BuildingContext context,
                                          DiceParser.LogicalExpressionContext expressionContext)
    {
        ParseTree decl = expressionContext.getChild(0);

        if (decl instanceof DiceParser.UnaryLogicalExpressionContext) {
            return buildUnaryBoolean(context, (DiceParser.UnaryLogicalExpressionContext)decl);
        }
        else if (decl instanceof DiceParser.LogicalAndExpressionContext) {
            return buildAnd(context, (DiceParser.LogicalAndExpressionContext)decl);
        }
        else if (decl instanceof DiceParser.LogicalOrExpressionContext) {
            return buildOr(context, (DiceParser.LogicalOrExpressionContext)decl);
        }

        return null;
    }

    private BooleanExpression buildAnd(BuildingContext context,
                                       DiceParser.LogicalAndExpressionContext expressionContext)
    {
        BooleanExpression lhs = buildUnaryBoolean(context,
                (DiceParser.UnaryLogicalExpressionContext)expressionContext.getChild(0));
        BooleanExpression rhs = buildBoolean(context,
                (DiceParser.LogicalExpressionContext)expressionContext.getChild(2));
        return And.of(lhs, rhs);
    }

    private BooleanExpression buildOr(BuildingContext context,
                                       DiceParser.LogicalOrExpressionContext expressionContext)
    {
        BooleanExpression lhs = buildUnaryBoolean(context,
                (DiceParser.UnaryLogicalExpressionContext)expressionContext.getChild(0));
        BooleanExpression rhs = buildBoolean(context,
                (DiceParser.LogicalExpressionContext)expressionContext.getChild(2));
        return Or.of(lhs, rhs);
    }

    private BooleanExpression buildUnaryBoolean(BuildingContext context,
                                                DiceParser.UnaryLogicalExpressionContext expressionContext)
    {
        ParseTree decl = expressionContext.getChild(0);

        if (decl instanceof DiceParser.RelationalExpressionContext) {
            return buildRelation(context, (DiceParser.RelationalExpressionContext)decl);
        }
        else if (decl instanceof DiceParser.LeftImplicitRelationalExpressionContext) {
            return buildLeftImplicitReleation(context, (DiceParser.LeftImplicitRelationalExpressionContext)decl);
        }
        else if (decl instanceof DiceParser.RightImplicitRelationalExpressionContext) {
            return buildRightImplicitReleation(context, (DiceParser.RightImplicitRelationalExpressionContext)decl);
        }
        else if (decl instanceof DiceParser.GroupLogicalExpressionContext) {
            return buildBoolean(context, (DiceParser.LogicalExpressionContext)decl.getChild(1));
        }
        else if (decl instanceof TerminalNode) {
            Token opToken = ((TerminalNode)decl).getSymbol();
            BooleanExpression child =
                    buildBoolean(context, (DiceParser.LogicalExpressionContext)expressionContext.getChild(1));

            if (opToken.getType() == DiceParser.NOT) {
                return Not.of(child);
            }
        }

        return null;
    }

    private BooleanExpression buildLeftImplicitReleation(BuildingContext context,
                                                         DiceParser.LeftImplicitRelationalExpressionContext expressionContext)
    {
        ArithmeticExpression lhs = null; // TODO
        ArithmeticExpression rhs =
                buildArithmetic(context, (DiceParser.ArithmeticExpressionContext)expressionContext.getChild(1));

        Token optoken = ((TerminalNode)expressionContext.getChild(0)).getSymbol();

        return Comparison.of(opFromToken(optoken), lhs, rhs);
    }

    private BooleanExpression buildRightImplicitReleation(BuildingContext context,
                                                         DiceParser.RightImplicitRelationalExpressionContext expressionContext)
    {
        ArithmeticExpression lhs =
                buildArithmetic(context, (DiceParser.ArithmeticExpressionContext)expressionContext.getChild(0));
        ArithmeticExpression rhs = null; //TODO

        Token optoken = ((TerminalNode)expressionContext.getChild(1)).getSymbol();

        return Comparison.of(opFromToken(optoken), lhs, rhs);
    }

    private BooleanExpression buildRelation(BuildingContext context,
                                            DiceParser.RelationalExpressionContext expressionContext)
    {
        ArithmeticExpression lhs =
                buildArithmetic(context, (DiceParser.ArithmeticExpressionContext)expressionContext.getChild(0));
        ArithmeticExpression rhs =
                buildArithmetic(context, (DiceParser.ArithmeticExpressionContext)expressionContext.getChild(2));

        Token optoken = ((TerminalNode)expressionContext.getChild(1)).getSymbol();

        return Comparison.of(opFromToken(optoken), lhs, rhs);
    }

    private String opFromToken(Token token) {
        String op = "";

        switch (token.getType()) {
            case DiceParser.GT: op = Comparison.GT; break;
            case DiceParser.GTE: op = Comparison.GTE; break;
            case DiceParser.LT: op = Comparison.LT; break;
            case DiceParser.LTE: op = Comparison.LTE; break;
            case DiceParser.EQ: op = Comparison.EQ; break;
            case DiceParser.NEQ: op = Comparison.NEQ; break;
        }

        return op;
    }

    public ArithmeticExpression buildArithmetic(BuildingContext context,
                                                DiceParser.ArithmeticExpressionContext expressionContext)
    {
        ParseTree decl = expressionContext.getChild(0);

        if (decl instanceof DiceParser.UnaryArithmeticExpressionContext) {
            return buildUnaryArithmetic(context, (DiceParser.UnaryArithmeticExpressionContext)decl);
        }
        else if (decl instanceof DiceParser.AdditiveExpressionContext) {
            return buildAdditiveExpressionContext(context, (DiceParser.AdditiveExpressionContext)decl);
        }
        else if (decl instanceof DiceParser.MultiplicativeExpressionContext) {
            return buildMultiplicativeExpressionContext(context, (DiceParser.MultiplicativeExpressionContext)decl);
        }

        return null;
    }

    private ArithmeticExpression buildUnaryArithmetic(BuildingContext context,
                                                      DiceParser.UnaryArithmeticExpressionContext expressionContext) {

        ParseTree decl = expressionContext.getChild(0);

        if (decl instanceof DiceParser.NumberContext) {
            return buildNumber((DiceParser.NumberContext)decl);
        }
        else if (decl instanceof DiceParser.RandomNumberContext) {
            return buildRandomNumber((DiceParser.RandomNumberContext)decl);
        }
        else if (decl instanceof DiceParser.ProbableNumberContext) {
            return buildProbableNumber((DiceParser.ProbableNumberContext)decl);
        }
        else if (decl instanceof DiceParser.GroupArithmeticExpressionContext) {
            return buildArithmetic(context, (DiceParser.ArithmeticExpressionContext)decl.getChild(1));
        }
        else if (decl instanceof TerminalNode) {
            Token token = ((TerminalNode)decl).getSymbol();
            ArithmeticExpression child = buildArithmetic(
                    context, (DiceParser.ArithmeticExpressionContext)expressionContext.getChild(1));
            if (token.getType() == DiceParser.MINUS)
                return Negation.of(child);
            else
                return child;
        }

        return null;
    }

    private ArithmeticExpression buildNumber(DiceParser.NumberContext numberContext) {
        Token token = ((TerminalNode)numberContext.getChild(0)).getSymbol();

        if (token.getType() == DiceParser.INT)
            return IntNumber.of(Integer.parseInt(token.getText()));
        else if (token.getType() == DiceParser.FRACTION) {
            String[] parts = token.getText().split("/");
            return FractionNumber.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }

        return IntNumber.of(1);
    }

    private ArithmeticExpression buildRandomNumber(DiceParser.RandomNumberContext context) {
        Token token = ((TerminalNode)context.getChild(0)).getSymbol();
        String text = token.getText();

        if (text.equals("D"))
            return DiceNumber.of(1, 6);
        else if (text.charAt(0) == 'D')
            return DiceNumber.of(1, Integer.parseInt(text.substring(1)));
        else if (text.charAt(text.length() - 1) == 'D')
            return DiceNumber.of(Integer.parseInt(text.substring(0, text.length() - 1)), 6);
        else {
            String[] parts = token.getText().split("D");
            return DiceNumber.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }

    private ArithmeticExpression buildProbableNumber(DiceParser.ProbableNumberContext context) {
        Token token = ((TerminalNode)context.getChild(0)).getSymbol();
        String text = token.getText();
        int percentage = Integer.parseInt(text.substring(0, text.length() - 1));
        return ProbableNumber.of(percentage * 1e-2f);
    }

    private ArithmeticExpression buildAdditiveExpressionContext(BuildingContext context,
                                                                DiceParser.AdditiveExpressionContext expressionContext) {
        ArithmeticExpression lhs = buildUnaryArithmetic(
                context, (DiceParser.UnaryArithmeticExpressionContext)expressionContext.getChild(0));
        ArithmeticExpression rhs = buildArithmetic(
                context,(DiceParser.ArithmeticExpressionContext)expressionContext.getChild(2));

        Token token = ((TerminalNode)expressionContext.getChild(1)).getSymbol();

        if (token.getType() == DiceParser.PLUS)
            return Addition.of(lhs, rhs);
        else
            return Subtraction.of(lhs, rhs);
    }

    private ArithmeticExpression buildMultiplicativeExpressionContext(BuildingContext context,
                                                                      DiceParser.MultiplicativeExpressionContext expressionContext) {
        ArithmeticExpression lhs = buildUnaryArithmetic(
                context, (DiceParser.UnaryArithmeticExpressionContext)expressionContext.getChild(0));
        ArithmeticExpression rhs = buildArithmetic(
                context,(DiceParser.ArithmeticExpressionContext)expressionContext.getChild(2));

        return Multiplication.of(lhs, rhs);
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
            else if (nodeSpec instanceof GateSpec) {
                node = new Gate();
            }
            else if (nodeSpec instanceof SourceSpec) {
                node = new Source();
            }
            else if (nodeSpec instanceof DrainSpec) {
                node = new Drain();
            }
            else if (nodeSpec instanceof EndSpec) {
                node = new End();
            }
            else if (nodeSpec instanceof TraderSpec) {
                node = new Trader();
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

    private void createExplicitActivators(BuildingContext context, YamlSpec spec) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        spec.getActivators().forEach(a -> {
            try {
                ActivatorBuildContext buildContext = getActivatorBuildContext(context, a);
                if (buildContext.owner == null)
                    throw new Exception(String.format("Owner node identifier is required for activator %s", a));
                createActivator(context, buildContext);
            }
            catch (Exception ex) {
                lastError.compareAndSet(null, ex);
            }
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createImplicitActivators(BuildingContext context) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        List<AbstractNode> nodes = context.machinations.getElements().stream()
                .filter(e -> e instanceof AbstractNode).map(e -> (AbstractNode)e).collect(Collectors.toList());

        nodes.forEach(node -> {
            NodeSpec spec = (NodeSpec)context.elementSpec.get(node);
            spec.getActivators().forEach(a -> {
                try {
                    ActivatorBuildContext buildContext = getActivatorBuildContext(context, a);
                    buildContext.owner = node;
                    createActivator(context, buildContext);
                }
                catch (Exception ex) {
                    lastError.compareAndSet(null, ex);
                }
            });
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createActivator(BuildingContext context, ActivatorBuildContext buildContext) {
        Activator activator = new Activator().setOwner(buildContext.owner).setTarget(buildContext.target)
                .setLabel(buildContext.condition.getText());
        buildContext.owner.getActivators().add(activator);
        context.buildContext.put(activator, buildContext);
    }

    private void createExplicitTrigger(BuildingContext context, YamlSpec spec) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        spec.getTriggers().forEach(t -> {
            try {
                TriggerBuildContext triggerBuildContext = getTriggerBuildContext(context, t);
                if (triggerBuildContext.owner == null)
                    throw new Exception(String.format("Owner node identifier is required for trigger %s", t));
                createTrigger(context, triggerBuildContext);
            }
            catch (Exception ex) {
                lastError.compareAndSet(null, ex);
            }
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createImplicitTrigger(BuildingContext context) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        List<AbstractNode> nodes = context.machinations.getElements().stream()
                .filter(e -> e instanceof AbstractNode).map(e -> (AbstractNode)e).collect(Collectors.toList());

        nodes.forEach(node -> {
            NodeSpec spec = (NodeSpec)context.elementSpec.get(node);
            spec.getTriggers().forEach(m -> {
                try {
                    TriggerBuildContext triggerBuildContext = getTriggerBuildContext(context, m);
                    triggerBuildContext.owner = node;
                    createTrigger(context, triggerBuildContext);
                }
                catch (Exception ex) {
                    lastError.compareAndSet(null, ex);
                }
            });
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createTrigger(BuildingContext context, TriggerBuildContext buildContext) {
        Trigger trigger = new Trigger().setOwner(buildContext.owner).setTarget(buildContext.target);
        buildContext.owner.getTriggers().add(trigger);
    }

    private void createExplicitModifiers(BuildingContext context, YamlSpec spec) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        spec.getModifiers().forEach(m -> {
            try {
                ModifierBuildContext modifierBuildContext = getModifierBuildContext(context, m);
                if (modifierBuildContext.owner == null)
                    throw new Exception(String.format("Owner node identifier is required for modifier %s", m));

                createModifier(context, modifierBuildContext);
            }
            catch (Exception ex) {
                lastError.compareAndSet(null, ex);
            }
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createImplicitModifiers(BuildingContext context) throws Exception {
        AtomicReference<Exception> lastError = new AtomicReference<>();

        List<AbstractNode> nodes = context.machinations.getElements().stream()
                .filter(e -> e instanceof AbstractNode).map(e -> (AbstractNode)e).collect(Collectors.toList());

        nodes.forEach(node -> {
            NodeSpec spec = (NodeSpec)context.elementSpec.get(node);
            spec.getModifiers().forEach(m -> {
                try {
                    ModifierBuildContext modifierBuildContext = getModifierBuildContext(context, m);
                    modifierBuildContext.owner = node;
                    createModifier(context, modifierBuildContext);
                }
                catch (Exception ex) {
                    lastError.compareAndSet(null, ex);
                }
            });
        });

        if (lastError.get() != null)
            throw lastError.get();
    }

    private void createModifier(BuildingContext context, ModifierBuildContext buildContext) throws Exception {
        Modifier modifier = new Modifier();
        modifier.setOwner(buildContext.owner).setTarget(buildContext.target).setLabel(buildContext.expression.getText());
        buildContext.owner.getModifiers().add(modifier);

        context.buildContext.putIfAbsent(modifier, buildContext);
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
                .setId(getOrCreateId(connectionBuildContext.id));

        if (connectionBuildContext.labelExpression != null)
            connection.setLabel(connectionBuildContext.labelExpression.getText());

        context.machinations.addElement(connection);
        context.buildContext.put(connection, connectionBuildContext);

        connectionBuildContext.from.getOutgoingConnections().add(connection);
        connectionBuildContext.to.getIncomingConnections().add(connection);
    }

    private ConnectionBuildContext getConnectionBuildContext(BuildingContext context, String definition) throws Exception {
        DiceParser parser = getParser(definition);
        ConnectionBuildContext buildContext = new ConnectionBuildContext();

        ParseTree decl = parser.connectionDefinition().children.get(0);
        int next = 0;
        ParseTree nextDecl = decl.getChild(next);

        if (decl instanceof DiceParser.ExplicitConnectionDefinitionContext) {
            buildContext.from = (AbstractNode)context.machinations.findById(nextDecl.getText());
            if (buildContext.from == null)
                throw new Exception(String.format("Unknown identifier %s", nextDecl.getText()));

            next += 2;
        }

        nextDecl = decl.getChild(next);
        if (nextDecl instanceof DiceParser.ExpressionContext) {
            buildContext.labelExpression = (DiceParser.ExpressionContext)nextDecl;
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

    private ModifierBuildContext getModifierBuildContext(BuildingContext context, String definition) throws Exception {
        DiceParser parser = getParser(definition);
        ModifierBuildContext buildContext = new ModifierBuildContext();

        ParseTree decl = parser.modifierDefinition();
        int next = 0;
        ParseTree nextDecl = decl.getChild(next);

        if (nextDecl instanceof TerminalNode) {
            buildContext.owner = (AbstractNode)context.machinations.findById(nextDecl.getText());
            if (buildContext.owner == null)
                throw new Exception(String.format("Unknown identifier %s", nextDecl.getText()));

            next += 2;
        }

        nextDecl = decl.getChild(next);
        buildContext.expression = (DiceParser.ArithmeticExpressionContext)nextDecl;
        next += 2;

        nextDecl = decl.getChild(next);
        buildContext.target = context.machinations.findById(nextDecl.getText());

        return buildContext;
    }

    private TriggerBuildContext getTriggerBuildContext(BuildingContext context, String definition) throws Exception {
        DiceParser parser = getParser(definition);
        TriggerBuildContext buildContext = new TriggerBuildContext();

        ParseTree decl = parser.triggerDefinition();
        int next = 0;
        ParseTree nextDecl = decl.getChild(next);

        if (decl.getChildCount() == 3) {
            buildContext.owner = (AbstractNode)context.machinations.findById(nextDecl.getText());
            if (buildContext.owner == null)
                throw new Exception(String.format("Unknown identifier %s", nextDecl.getText()));

            next = 2;
        }
        else
            next = 1;

        nextDecl = decl.getChild(next);
        buildContext.target = context.machinations.findById(nextDecl.getText());

        return buildContext;
    }

    private ActivatorBuildContext getActivatorBuildContext(BuildingContext context, String definition) throws Exception {
        DiceParser parser = getParser(definition);
        ActivatorBuildContext buildContext = new ActivatorBuildContext();

        ParseTree decl = parser.activatorDefinition();
        int next = 0;
        ParseTree nextDecl = decl.getChild(next);

        if (nextDecl instanceof TerminalNode) {
            buildContext.owner = (AbstractNode)context.machinations.findById(nextDecl.getText());
            if (buildContext.owner == null)
                throw new Exception(String.format("Unknown identifier %s", nextDecl.getText()));

            next = 2;
        }

        nextDecl = decl.getChild(next);
        buildContext.condition = (DiceParser.LogicalExpressionContext)nextDecl;
        next += 2;

        nextDecl = decl.getChild(next);
        buildContext.target = (AbstractNode)context.machinations.findById(nextDecl.getText());

        return buildContext;
    }

    private DiceParser getParser(String expression) {
        CharStream stream = new ANTLRInputStream(expression);
        TokenStream tokens = new CommonTokenStream(new DiceLexer(stream));

        DiceParser parser = new DiceParser(tokens);
        return parser;
    }

    private void buildResourcesDecl(AbstractNode node, String resourceExpression) {
//        Map<String, Integer> nodeResources = node.getResources();
//        nodeResources.clear();

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

                node.resourceContainer.add(name, count);

//                if (nodeResources.putIfAbsent(name, count) != null)
//                    nodeResources.compute(name, (n, c0) -> (c0 + count));
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
