package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.*;
import com.squarebit.machinations.machc.parsers.MachLexer;
import com.squarebit.machinations.machc.parsers.MachParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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
        graph.setId(graphDeclarationContext.getChild(1).getText());

        graphTransformationContext.setGraph(graph);

        ParseTree graphBodyContext = graphDeclarationContext.getChild(2);
        for (int i = 1; i < graphBodyContext.getChildCount() - 1; i++) {
            ParseTree decl = graphBodyContext.getChild(i).getChild(0);

            if (decl instanceof MachParser.GraphFieldDeclarationContext) {
                List<GGraphField> fields = transformGraphFieldDeclaration(
                        graphTransformationContext, (MachParser.GraphFieldDeclarationContext)decl);

                for (GGraphField field : fields) {
                    graph.addField(field);
                }
            }
            else if (decl instanceof MachParser.NodeDeclarationContext) {
                // transformNodeDeclaration(graphTransformationContext, (MachParser.NodeDeclarationContext)decl);
            }

        }

        return graph;
    }

    /**
     * Transform a graph field declaration context to a list of field declaration.
     * @param graphTransformationContext current graph transformation context
     * @param graphFieldDeclarationContext field declaration context
     * @return a list of declared field
     */
    private List<GGraphField> transformGraphFieldDeclaration(GGraphTransformationContext graphTransformationContext,
                                                             MachParser.GraphFieldDeclarationContext graphFieldDeclarationContext)
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

            GField field = new GField();
            field.setId(name.getText());
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

    private GExpression transformExpression(Scope scope,
                                            MachParser.ExpressionContext expressionContext) throws Exception
    {
        return null;
    }
}
