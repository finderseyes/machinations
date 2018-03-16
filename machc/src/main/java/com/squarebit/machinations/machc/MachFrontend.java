package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.ast.expressions.*;
import com.squarebit.machinations.machc.ast.statements.GReturn;
import com.squarebit.machinations.machc.parsers.MachLexer;
import com.squarebit.machinations.machc.parsers.MachParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The frontend of Mach language.
 */
public class MachFrontend {
    /**
     * Compiles a set of source files to a program.
     *
     * @param paths the source file paths
     * @return the program
     * @throws Exception the exception
     */
    public GProgram compile(String[] paths) throws Exception {
        GProgram program = new GProgram();

        for (int i = 0; i < paths.length; i++) {
            GUnit unit = compileUnit(paths[i]);
            program.addUnit(unit);
        }

        return program;
    }

    /**
     * Compiles a source unit to a program
     *
     * @param path the source file path
     * @return the program
     * @throws Exception the exception
     */
    public GProgram compile(String path) throws Exception {
        return compile(new String[]{ path });
    }

    /**
     * Compile a file to a program unit.
     *
     * @param path the path
     * @return the program unit
     * @throws Exception the exception
     */
    public GUnit compileUnit(String path) throws Exception {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        ANTLRInputStream inputStream = new ANTLRInputStream(fileInputStream);
        MachLexer lexer = new MachLexer(inputStream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        MachParser parser = new MachParser(tokenStream);

        return transformUnit(parser.unitDeclaration());
    }

    /**
     * Literally transforms a unit context to a program unit.
     * @param unitDeclarationContext the unit context
     * @return a program unit
     * @throws Exception exception if yet.
     */
    private GUnit transformUnit(MachParser.UnitDeclarationContext unitDeclarationContext) throws Exception {
        GUnitTransformationContext unitTransformationContext = new GUnitTransformationContext();

        GUnit unit = new GUnit();

        for (int i = 0; i < unitDeclarationContext.getChildCount(); i++) {
            ParseTree decl = unitDeclarationContext.getChild(i).getChild(0);

            if (decl instanceof MachParser.GraphDeclarationContext) {
                GGraph graph = transformGraph(
                        unitTransformationContext,
                        (MachParser.GraphDeclarationContext)decl
                );
                unit.addGraph(graph);
            }
        }

        return unit;
    }

    /**
     * Literally transforms a graph declaration to a graph.
     * @param unitTransformationContext the current unit transformation context
     * @param graphDeclarationContext the graph declaration context
     * @return
     * @throws Exception
     */
    private GGraph transformGraph(GUnitTransformationContext unitTransformationContext,
                                  MachParser.GraphDeclarationContext graphDeclarationContext)
        throws Exception
    {
        GGraphTransformationContext graphTransformationContext =
                new GGraphTransformationContext().setUnitTransformationContext(unitTransformationContext);

        GGraph graph = new GGraph();

        int next = 0;
        ParseTree graphDecl = graphDeclarationContext.getChild(0);

        if (graphDecl instanceof MachParser.GraphModifierContext) {
            if (graphDecl.getText().equals("default"))
                graph.setDefaultGraph(true);

            next += 2;
            graphDecl = graphDeclarationContext.getChild(next);
        }
        else {
            next += 1;
            graphDecl = graphDeclarationContext.getChild(next);
        }

        graph.setName(graphDecl.getText());
        graphTransformationContext.setGraph(graph);

        next += 1;
        graphDecl = graphDeclarationContext.getChild(next);

        if (graphDecl instanceof MachParser.BaseGraphDescriptorContext) {
            next += 1;
            graphDecl = graphDeclarationContext.getChild(next);
        }

        ParseTree graphBodyContext = graphDecl;
        for (int i = 1; i < graphBodyContext.getChildCount() - 1; i++) {
            ParseTree decl = graphBodyContext.getChild(i).getChild(0);

            if (decl instanceof MachParser.GraphFieldDeclarationContext) {
                List<GGraphField> fields = transformGraphFieldDeclaration(
                        graphTransformationContext, (MachParser.GraphFieldDeclarationContext)decl);

                for (GGraphField field : fields) {
                    graph.addField(field);
                }
            }
            else if (decl instanceof MachParser.MethodDeclarationContext) {
                GMethod method = transformMethodDeclaration((MachParser.MethodDeclarationContext)decl);
                graph.addMethod(method);
            }
        }

        return graph;
    }

    /**
     *
     * @param methodDeclarationContext
     * @return
     * @throws Exception
     */
    private GMethod transformMethodDeclaration(MachParser.MethodDeclarationContext methodDeclarationContext)
        throws Exception
    {
        GMethod method = new GMethod();

        int next = 0;
        ParseTree decl = methodDeclarationContext.getChild(next);

        if (decl instanceof MachParser.MethodModifierContext) {
            next += 1;
            decl = methodDeclarationContext.getChild(next);
        }

        String methodName = decl.getText();
        method.setName(methodName);

        next += 2;
        decl = methodDeclarationContext.getChild(next);

        if (decl instanceof MachParser.MethodArgumentDeclaratorListContext) {
            for (int i = 0; i < decl.getChildCount(); i += 2)
                method.addArgument(decl.getChild(i).getText());

            next += 2;
            decl = methodDeclarationContext.getChild(next);
        }
        else {
            next += 1;
            decl = methodDeclarationContext.getChild(next);
        }

        ParseTree statementsContext = decl.getChild(1);
        if (statementsContext instanceof MachParser.BlockStatementsContext) {
            List<GStatement> statements = transformBlockStatements((MachParser.BlockStatementsContext)statementsContext);
            statements.forEach(method::addStatement);
        }

        return method;
    }

    /**
     * Transform block statements to a list.
     * @param context
     * @return
     * @throws Exception
     */
    private List<GStatement> transformBlockStatements(MachParser.BlockStatementsContext context)
        throws Exception
    {
        List<GStatement> statements = new ArrayList<>();
        for (int i = 0; i < context.getChildCount(); i++) {
            ParseTree decl = context.getChild(i);
            GStatement statement = transformBlockStatement((MachParser.BlockStatementContext)decl);
            if (statement != GStatement.EMPTY)
                statements.add(statement);
        }
        return statements;
    }

    /**
     *
     * @param context
     * @return
     * @throws Exception
     */
    private GStatement transformBlockStatement(MachParser.BlockStatementContext context)
        throws Exception
    {
        ParseTree decl = context.getChild(0);

        if (decl instanceof MachParser.StatementContext) {
            return transformStatement((MachParser.StatementContext)decl);
        }
        else
            return null;
    }

    /**
     *
     * @param context
     * @return
     * @throws Exception
     */
    private GStatement transformStatement(MachParser.StatementContext context) throws Exception {
        ParseTree decl = context.getChild(0);

        if (decl instanceof MachParser.EmptyStatementContext)
            return GStatement.EMPTY;
        else if (decl instanceof MachParser.BlockContext) {
            return null;
        }
        else if (decl instanceof MachParser.ExpressionStatementContext) {
            return null;
        }
        else if (decl instanceof MachParser.IfThenStatementContext ||
                decl instanceof MachParser.IfThenElseStatementContext)
        {
            return null;
        }
        else if (decl instanceof MachParser.ReturnStatementContext) {
            ParseTree returnExpression = decl.getChild(1);
            if (returnExpression instanceof MachParser.ExpressionContext)
                return new GReturn(transformExpression((MachParser.ExpressionContext)returnExpression));
            else
                return new GReturn();
        }
        else
            throw new Exception("Shall not reach here");
    }

    /**
     * Transform a graph field declaration context to a list of field declaration.
     * @param graphTransformationContext current graph transformation context
     * @param graphFieldDeclarationContext field declaration context
     * @return a list of declared field
     */
    private List<GGraphField> transformGraphFieldDeclaration(
            GGraphTransformationContext graphTransformationContext,
            MachParser.GraphFieldDeclarationContext graphFieldDeclarationContext)
        throws Exception
    {
        ParseTree decl = graphFieldDeclarationContext.getChild(0);

        if (decl instanceof MachParser.FieldDeclarationContext) {
            List<GField> fields = transformFieldDeclaration(
                    graphTransformationContext,
                    (MachParser.FieldDeclarationContext)decl);
            return fields.stream().map(f -> (GGraphField)f).collect(Collectors.toList());
        }

        throw new RuntimeException("Should not reach here.");
    }

    /**
     *
     * @param graphTransformationContext
     * @param fieldDeclarationContext
     * @return
     */
    private List<GField> transformFieldDeclaration(GGraphTransformationContext graphTransformationContext,
                                                        MachParser.FieldDeclarationContext fieldDeclarationContext)
        throws Exception
    {
        List<GField> fields = new ArrayList<>();

        MachParser.VariableDeclarationListContext variableDeclarationListContext =
                (MachParser.VariableDeclarationListContext)fieldDeclarationContext.getChild(0).getChild(1);

        for (int i = 0; i < variableDeclarationListContext.getChildCount(); i+=2) {
            MachParser.VariableDeclaratorContext declarator =
                    (MachParser.VariableDeclaratorContext)variableDeclarationListContext.getChild(i);

            MachParser.VariableNameContext name =
                    (MachParser.VariableNameContext)declarator.getChild(0);

            MachParser.VariableInitializerContext initializer =
                    (declarator.getChildCount() == 3) ?
                            (MachParser.VariableInitializerContext)declarator.getChild(2) :
                            null
                    ;

            GExpression initializerExpression =
                    initializer != null ?
                            transformExpression((MachParser.ExpressionContext)initializer.getChild(0)) :
                            null;

            GField field = new GField();
            field.setInitializer(initializerExpression).setName(name.getText());
            fields.add(field);
        }


        return fields;
    }

    /**
     * Transform node declaration.
     *
     * @param graphTransformationContext
     * @param nodeDeclarationContext
     * @return
     * @throws Exception
     */
    private List<GNode> transformNodeDeclaration(GGraphTransformationContext graphTransformationContext,
                                           MachParser.NodeDeclarationContext nodeDeclarationContext)
        throws Exception
    {
        List<GNode> nodes = new ArrayList<>();

//        int next = 0;
//        ParseTree decl = nodeDeclarationContext.getChild(next);
//        MachParser.NodeModifiersContext nodeModifiersContext = null;
//
//        if (decl instanceof MachParser.NodeModifiersContext) {
//            nodeModifiersContext = (MachParser.NodeModifiersContext)decl;
//
//            next += 2;
//            decl = nodeDeclarationContext.getChild(next);
//        }
//        else {
//            next += 1;
//            decl = nodeDeclarationContext.getChild(next);
//        }
//
//        if (decl instanceof MachParser.NodeDeclaratorListContext) {
//            for (int i = 0; i < decl.getChildCount(); i += 2) {
//                GNode node = transformNodeDeclarator(
//                        graphTransformationContext,
//                        nodeModifiersContext,
//                        (MachParser.NodeDeclaratorContext)decl.getChild(i)
//                );
//
//                graphTransformationContext.getGraph().addField(node);
//                nodes.add(node);
//            }
//        }

        return nodes;
    }

//    /**
//     * Populates node modifiers to node.
//     * @param modifiersContext the node modifiers
//     */
//    private GNode.Modifier transformNodeModifiers(MachParser.NodeModifiersContext modifiersContext) throws Exception {
//        GNode.Modifier modifier = new GNode.Modifier();
//
//        for (int i = 0; i < modifiersContext.getChildCount(); i++) {
//            ParseTree delc = modifiersContext.getChild(i).getChild(0);
//            String value = delc.getText();
//
//            if (value.equals("interactive")) {
//                if (modifier.isInterative())
//                    throw new CompilationException(String.format(ErrorMessages.INCOMPATIBLE_NODE_MODIFIERS, value));
//                else
//                    modifier.setInterative(true);
//            }
//            else if (value.equals("transitive")) {
//                if (modifier.isTransitive())
//                    throw new CompilationException(String.format(ErrorMessages.INCOMPATIBLE_NODE_MODIFIERS, value));
//                else
//                    modifier.setTransitive(true);
//            }
//            else
//                throw new CompilationException(String.format(ErrorMessages.UNKNOWN_NODE_IDENTIFIER, value));
//
//        }
//
//        return modifier;
//    }
//
//    /**
//     * Transform a node declarator to a node.
//     * @param graphTransformationContext the graph context
//     * @param nodeDeclaratorContext the node declarator
//     * @return
//     * @throws Exception
//     */
//    private GNode transformNodeDeclarator(GGraphTransformationContext graphTransformationContext,
//                                           MachParser.NodeModifiersContext nodeModifiersContext,
//                                           MachParser.NodeDeclaratorContext nodeDeclaratorContext) throws Exception
//    {
//        GNode node = new GNode();
//
//        GNodeTransformationContext nodeTransformationContext =
//                new GNodeTransformationContext().setNode(node);
//
//        if (nodeModifiersContext != null)
//            node.setModifier(transformNodeModifiers(nodeModifiersContext));
//
//        node.setId(nodeDeclaratorContext.getChild(0).getText());
//
//        ParseTree decl = nodeDeclaratorContext.getChild(2);
//        if (decl instanceof MachParser.NodeInitializerContext) {
//            node.setInitializer(
//                    transformNodeInitializer(nodeTransformationContext, (MachParser.NodeInitializerContext)decl)
//            );
//        }
//
//        return node;
//    }

//    /**
//     *
//     * @param nodeTransformationContext
//     * @param nodeInitializerContext
//     * @return
//     * @throws Exception
//     */
//    private GNode.Initializer transformNodeInitializer(GNodeTransformationContext nodeTransformationContext,
//                                                       MachParser.NodeInitializerContext nodeInitializerContext)
//        throws Exception
//    {
//        ParseTree decl = nodeInitializerContext.getChild(0);
//
//        if (decl instanceof MachParser.SourceNodeInitializerContext)
//            return GNode.SOURCE_INITIALIZER;
//        else if (decl instanceof MachParser.DrainNodeInitializerContext)
//            return GNode.DRAIN_INITIALIZER;
//        else {
//            return null;
////            MachParser.ResourceSetExpressionContext resourceSetExpressionContext =
////                    (MachParser.ResourceSetExpressionContext)decl.getChild(0);
////
////            GResourceSet resourceSet = new GResourceSet();
////
////            if (resourceSetExpressionContext.getChildCount() == 1) {
////                GResourceDescriptor descriptor = transformResourceDescriptor(
////                        nodeTransformationContext,
////                        (MachParser.ResourceDescriptorContext)resourceSetExpressionContext.getChild(0)
////                );
////                resourceSet.addDescriptor(descriptor);
////            }
////            else {
////                for (int i = 1; i < resourceSetExpressionContext.getChildCount() - 1; i += 2) {
////                    GResourceDescriptor descriptor = transformResourceDescriptor(
////                            nodeTransformationContext,
////                            (MachParser.ResourceDescriptorContext)resourceSetExpressionContext.getChild(i)
////                    );
////                    resourceSet.addDescriptor(descriptor);
////                }
////            }
////
////            return new GNode.Initializer().setResourceSet(resourceSet);
//        }
//    }

//    /**
//     *
//     * @param nodeTransformationContext
//     * @param resourceDescriptorContext
//     * @return
//     * @throws Exception
//     */
//    private GResourceDescriptor transformResourceDescriptor(
//            GNodeTransformationContext nodeTransformationContext,
//            MachParser.ResourceDescriptorContext resourceDescriptorContext)
//        throws Exception
//    {
//        GResourceDescriptor descriptor = new GResourceDescriptor();
//
//        int next = 1;
//        ParseTree decl = resourceDescriptorContext.getChild(next);
//
//        if (decl instanceof TerminalNode) {
//            int capacity = Integer.parseInt(resourceDescriptorContext.getChild(next + 1).getText());
//            descriptor.setCapacity(capacity);
//
//            next += 2;
//            decl = resourceDescriptorContext.getChild(next);
//        }
//
//        if (decl instanceof MachParser.ResourceNameContext)
//            descriptor.setResourceName(decl.getText());
//
//        return descriptor;
//    }

    /**
     * Transform an expression.
     * @param expressionContext expression context
     * @return the expression
     * @throws Exception any exception found.
     */
    private GExpression transformExpression(MachParser.ExpressionContext expressionContext)
        throws Exception
    {
        ParseTree decl = expressionContext.getChild(0);

        if (decl instanceof MachParser.ConditionalExpressionContext) {
            return transformConditionalExpression((MachParser.ConditionalExpressionContext)decl);
        }
        else {
            return transformAssignmentExpression((MachParser.AssignmentContext)decl);
        }
    }

    /**
     * Transform a conditional expression
     * @param expressionContext expression context
     * @return the expression
     * @throws Exception any exception found
     */
    private GExpression transformConditionalExpression(MachParser.ConditionalExpressionContext expressionContext)
        throws Exception
    {
        MachParser.ConditionalOrExpressionContext orContext =
                (MachParser.ConditionalOrExpressionContext)expressionContext.getChild(0);

        GExpression orExpression = transformConditionalOrExpression(orContext);
        GExpression whenTrueExpression = null;
        GExpression whenFalseExpression = null;

        if (expressionContext.getChildCount() > 1) {
            whenTrueExpression = transformExpression(
                    (MachParser.ExpressionContext)expressionContext.getChild(2)
            );

            whenFalseExpression = transformConditionalExpression(
                    (MachParser.ConditionalExpressionContext)expressionContext.getChild(4)
            );
        }

        if (whenTrueExpression == null || whenFalseExpression == null)
            return orExpression;

        return new GTernaryExpression()
                .setCondition(orExpression)
                .setFirst(whenTrueExpression)
                .setSecond(whenFalseExpression);
    }

    /**
     * Transform a conditional "or" expression.
     * @param expressionContext the expression context
     * @return the expression
     * @throws Exception any exception found.
     */
    private GExpression transformConditionalOrExpression(MachParser.ConditionalOrExpressionContext expressionContext)
        throws Exception
    {
        ParseTree first = expressionContext.getChild(0);
        ParseTree second = expressionContext.getChild(2);

        if (second == null) {
            return transformConditionalAndExpression((MachParser.ConditionalAndExpressionContext)first);
        }
        else {
            GExpression orExpression =
                    transformConditionalOrExpression((MachParser.ConditionalOrExpressionContext)first);
            GExpression andExpression =
                    transformConditionalAndExpression((MachParser.ConditionalAndExpressionContext)second);

            return new GBinaryExpression().setOperator("||").setFirst(orExpression).setSecond(andExpression);
        }
    }

    /**
     * Transform conditional "and" expression.
     * @param expressionContext the expression context
     * @return the expression.
     * @throws Exception
     */
    private GExpression transformConditionalAndExpression(MachParser.ConditionalAndExpressionContext expressionContext)
        throws Exception
    {
        ParseTree first = expressionContext.getChild(0);
        ParseTree second = expressionContext.getChild(2);

        if (second == null) {
            return transformInclusiveOrExpression((MachParser.InclusiveOrExpressionContext)first);
        }
        else {
            GExpression andExpression =
                    transformConditionalAndExpression((MachParser.ConditionalAndExpressionContext)first);
            GExpression inclusiveOrExpression =
                    transformInclusiveOrExpression((MachParser.InclusiveOrExpressionContext)second);

            return new GBinaryExpression().setOperator("&&").setFirst(andExpression).setSecond(inclusiveOrExpression);
        }
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformInclusiveOrExpression(MachParser.InclusiveOrExpressionContext expressionContext)
        throws Exception
    {
        ParseTree first = expressionContext.getChild(0);
        ParseTree second = expressionContext.getChild(2);

        if (second == null) {
            return transformExclusiveOrExpression((MachParser.ExclusiveOrExpressionContext)first);
        }
        else {
            GExpression firstExp = transformInclusiveOrExpression((MachParser.InclusiveOrExpressionContext)first);
            GExpression secondExp = transformExclusiveOrExpression((MachParser.ExclusiveOrExpressionContext)second);

            return new GBinaryExpression().setOperator("|").setFirst(firstExp).setSecond(secondExp);
        }
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformExclusiveOrExpression(MachParser.ExclusiveOrExpressionContext expressionContext)
        throws Exception
    {
        ParseTree first = expressionContext.getChild(0);
        ParseTree second = expressionContext.getChild(2);

        if (second == null) {
            return transformAndExpression((MachParser.AndExpressionContext)first);
        }
        else {
            GExpression firstExp = transformExclusiveOrExpression((MachParser.ExclusiveOrExpressionContext)first);
            GExpression secondExp = transformAndExpression((MachParser.AndExpressionContext)second);

            return new GBinaryExpression().setOperator("^").setFirst(firstExp).setSecond(secondExp);
        }
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformAndExpression(MachParser.AndExpressionContext expressionContext)
        throws Exception
    {
        ParseTree first = expressionContext.getChild(0);
        ParseTree second = expressionContext.getChild(2);

        if (second == null) {
            return transformEqualityExpression((MachParser.EqualityExpressionContext)first);
        }
        else {
            GExpression firstExp = transformAndExpression((MachParser.AndExpressionContext)first);
            GExpression secondExp = transformEqualityExpression((MachParser.EqualityExpressionContext)second);

            return new GBinaryExpression().setOperator("&").setFirst(firstExp).setSecond(secondExp);
        }
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformEqualityExpression(MachParser.EqualityExpressionContext expressionContext)
        throws Exception
    {
        ParseTree first = expressionContext.getChild(0);
        ParseTree second = expressionContext.getChild(2);

        if (second == null) {
            return transformRelationalExpression((MachParser.RelationalExpressionContext)first);
        }
        else {
            GExpression firstExp = transformEqualityExpression((MachParser.EqualityExpressionContext)first);
            GExpression secondExp = transformRelationalExpression((MachParser.RelationalExpressionContext)second);
            String operator = expressionContext.getChild(1).getText();

            return new GBinaryExpression().setOperator(operator).setFirst(firstExp).setSecond(secondExp);
        }
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformRelationalExpression(MachParser.RelationalExpressionContext expressionContext)
        throws Exception
    {
        ParseTree first = expressionContext.getChild(0);
        ParseTree second = expressionContext.getChild(2);

        if (second == null) {
            return transformAdditiveExpression((MachParser.AdditiveExpressionContext)first);
        }
        else {
            GExpression firstExp = transformRelationalExpression((MachParser.RelationalExpressionContext)first);
            GExpression secondExp = transformAdditiveExpression((MachParser.AdditiveExpressionContext)second);
            String operator = expressionContext.getChild(1).getText();

            return new GBinaryExpression().setOperator(operator).setFirst(firstExp).setSecond(secondExp);
        }
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformAdditiveExpression(MachParser.AdditiveExpressionContext expressionContext)
        throws Exception
    {
        ParseTree first = expressionContext.getChild(0);
        ParseTree second = expressionContext.getChild(2);

        if (second == null) {
            return transformMultiplicativeExpression((MachParser.MultiplicativeExpressionContext)first);
        }
        else {
            GExpression firstExp = transformAdditiveExpression((MachParser.AdditiveExpressionContext)first);
            GExpression secondExp = transformMultiplicativeExpression((MachParser.MultiplicativeExpressionContext)second);
            String operator = expressionContext.getChild(1).getText();

            return new GBinaryExpression().setOperator(operator).setFirst(firstExp).setSecond(secondExp);
        }
    }

    private GExpression transformMultiplicativeExpression(MachParser.MultiplicativeExpressionContext expressionContext)
        throws Exception
    {
        ParseTree first = expressionContext.getChild(0);
        ParseTree second = expressionContext.getChild(2);

        if (second == null) {
            return transformUnaryExpression((MachParser.UnaryExpressionContext)first);
        }
        else {
            GExpression firstExp = transformMultiplicativeExpression((MachParser.MultiplicativeExpressionContext)first);
            GExpression secondExp = transformUnaryExpression((MachParser.UnaryExpressionContext)second);
            String operator = expressionContext.getChild(1).getText();

            return new GBinaryExpression().setOperator(operator).setFirst(firstExp).setSecond(secondExp);
        }
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformUnaryExpression(MachParser.UnaryExpressionContext expressionContext)
        throws Exception
    {
        ParseTree decl = expressionContext.getChild(0);

        if (decl instanceof MachParser.PreIncrementExpressionContext ||
                decl instanceof MachParser.PreDecrementExpressionContext) {
            return transformPrefixExpression(decl);
        }
        else if (decl instanceof MachParser.UnaryExpressionNotPlusMinusContext) {
            return transformUnaryExpressionNotPlusMinus(
                    (MachParser.UnaryExpressionNotPlusMinusContext)decl
            );
        }
        else {
            GExpression child = transformUnaryExpression(
                    (MachParser.UnaryExpressionContext)expressionContext.getChild(1)
            );

            if (decl.getText().equals("-")) {
                return new GUnaryExpression().setOperator("-").setChild(child);
            }
            else
                return child;
        }
    }

    /**
     *
     * @param decl
     * @return
     * @throws Exception
     */
    private GExpression transformPrefixExpression(ParseTree decl) throws Exception {
        GExpression child = transformUnaryExpression((MachParser.UnaryExpressionContext)decl.getChild(1));
        return new GPrefixExpression().setOperator(decl.getChild(0).getText()).setChild(child);
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformUnaryExpressionNotPlusMinus(MachParser.UnaryExpressionNotPlusMinusContext expressionContext)
        throws Exception
    {
        if (expressionContext.getChildCount() == 1) {
            return transformPostfixExpression((MachParser.PostfixExpressionContext)expressionContext.getChild(0));
        }
        else {
            GExpression child = transformUnaryExpression((MachParser.UnaryExpressionContext)expressionContext.getChild(1));
            return new GUnaryExpression().setOperator(expressionContext.getChild(0).getText()).setChild(child);
        }
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformPostfixExpression(MachParser.PostfixExpressionContext expressionContext)
        throws Exception
    {
        ParseTree decl = expressionContext.getChild(0);
        GExpression expression = null;

        if (decl instanceof MachParser.PrimaryContext) {
            expression = transformPrimary((MachParser.PrimaryContext)decl);
        }
        else {
            expression = new GSymbolRef(decl.getText());
        }

        for (int i = 1; i < expressionContext.getChildCount(); i++) {
            decl = expressionContext.getChild(i);
            expression = new GSuffixExpression().setOperator(decl.getText()).setChild(expression);
        }

        return expression;
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformPrimary(MachParser.PrimaryContext expressionContext)
        throws Exception
    {
        GExpression reference = transformPrimaryReference(
                (MachParser.PrimaryReferenceContext)expressionContext.getChild(0)
        );

        for (int i = 1; i < expressionContext.getChildCount(); i++) {
            ParseTree decl = expressionContext.getChild(i).getChild(0);

            if (decl instanceof MachParser.ReferenceFieldAccessContext)
                reference = loadField(reference, (MachParser.ReferenceFieldAccessContext)decl);
            else
                throw new RuntimeException("Not implemented");
        }

        return reference;
    }

    /**
     *
     * @param reference
     * @param fieldAccessContext
     * @return
     * @throws Exception
     */
    private GExpression loadField(GExpression reference, MachParser.ReferenceFieldAccessContext fieldAccessContext)
        throws Exception
    {
        return new GLoadField(reference, fieldAccessContext.getChild(1).getText());
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformPrimaryReference(MachParser.PrimaryReferenceContext expressionContext)
        throws Exception
    {
        ParseTree decl = expressionContext.getChild(0);

        if (decl instanceof MachParser.LiteralContext) {
            return transformLiteral((MachParser.LiteralContext)decl);
        }
        else if (decl.getText().equals("(")) {
            return transformExpression((MachParser.ExpressionContext)expressionContext.getChild(1));
        }
        else if (decl instanceof MachParser.ThisReferenceContext) {
            return GThis.INSTANCE;
        }
        else if (decl instanceof MachParser.LocalVariableOrThisFieldContext) {
            return new GSymbolRef(decl.getText());
        }
        else if (decl instanceof MachParser.ThisMethodInvocationContext) {
            return transformMethodInvocationDeclarator(
                    GThis.INSTANCE,
                    (MachParser.MethodInvocationDeclaratorContext) decl.getChild(0)
            );
        }
        else
            throw new RuntimeException("Not implemented");
    }

    /**
     *
     * @param context
     * @return
     * @throws Exception
     */
    private GExpression transformMethodInvocationDeclarator(GExpression reference,
                                                            MachParser.MethodInvocationDeclaratorContext context)
        throws Exception
    {
        ParseTree decl = context.getChild(0);

        String name = decl.getText();
        List<GExpression> arguments = Collections.emptyList();

        decl = context.getChild(2);
        if (decl instanceof MachParser.ArgumentListContext)
            arguments = transformArgumentList((MachParser.ArgumentListContext)decl);

        return new GMethodInvocation(reference, name, arguments.toArray(new GExpression[0]));
    }

    /**
     *
     * @param context
     * @return
     * @throws Exception
     */
    private GExpression transformLiteral(MachParser.LiteralContext context) throws Exception {
        ParseTree decl = context.getChild(0);

        if (decl instanceof MachParser.IntegralLiteralContext) {
            return GInteger.parse(decl.getText());
        }
        else if (decl instanceof MachParser.RandomIntegralLiteralContext) {
            return GRandomDice.parse(decl.getText());
        }
        else if (decl instanceof MachParser.FloatingPointLiteralContext) {
            return GFloat.parse(decl.getText());
        }
        else if (decl instanceof MachParser.BooleanLiteralContext) {
            return GBoolean.parse(decl.getText());
        }
        else {
            String text = decl.getText();
            return new GString(text.substring(1, text.length() - 1));
        }
    }

    /**
     * Transform a conditional expression
     * @param expressionContext expression context
     * @return the expression
     * @throws Exception any exception found
     */
    private GExpression transformAssignmentExpression(MachParser.AssignmentContext expressionContext)
        throws Exception
    {
        return  null;
    }

    /**
     *
     * @param expressionContext
     * @return
     * @throws Exception
     */
    private GExpression transformMethodInvocation(MachParser.MethodInvocationContext expressionContext)
        throws Exception
    {
        ParseTree decl = expressionContext.getChild(0);

        if (decl instanceof MachParser.GraphicalMethodInvocationContext) {
            return null;
        }
        else {
            GSymbolRef target = null;
            String name = null;
            List<GExpression> arguments = Collections.emptyList();

            if (decl instanceof MachParser.MethodNameContext) {
                name = decl.getText();
                decl = expressionContext.getChild(2);
            }
            else {
                name = expressionContext.getChild(2).getText();
                target = transformExpressionName((MachParser.ExpressionNameContext)decl);
                decl = expressionContext.getChild(4);
            }

            if (decl instanceof MachParser.ArgumentListContext)
                arguments = transformArgumentList((MachParser.ArgumentListContext)decl);

            return new GMethodCall(target, name, arguments.toArray(new GExpression[0]));
        }
    }

    /**
     *
     * @param expressionContext
     * @return
     */
    private GSymbolRef transformExpressionName(MachParser.ExpressionNameContext expressionContext) throws Exception {
        GSymbolRef ref = new GSymbolRef(expressionContext.getChild(0).getText());
        GSymbolRef current = ref;

        for (int i = 2; i < expressionContext.getChildCount(); i+=2) {
            GSymbolRef next = new GSymbolRef(expressionContext.getChild(i).getText());
            current.setNext(next);
            current = next;
        }

        return ref;
    }

    /**
     *
     * @param context
     * @return
     * @throws Exception
     */
    private List<GExpression> transformArgumentList(MachParser.ArgumentListContext context) throws Exception {
        List<GExpression> expressions = new ArrayList<>();

        for (int i = 0; i < context.getChildCount(); i++) {
            GExpression exp = transformExpression((MachParser.ExpressionContext)context.getChild(i));
            expressions.add(exp);
        }
        return expressions;
    }
}
