package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.GGraph;
import com.squarebit.machinations.machc.ast.GUnit;
import com.squarebit.machinations.machc.parsers.MachLexer;
import com.squarebit.machinations.machc.parsers.MachParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileInputStream;

/**
 * The frontend of Mach language.
 */
public class MachFrontend {
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
        UnitTransformationContext unitTransformationContext = new UnitTransformationContext();

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
    private GGraph transformGraph(UnitTransformationContext unitTransformationContext,
                                  MachParser.GraphDeclarationContext graphDeclarationContext)
            throws Exception
    {
        GGraph graph = new GGraph();

        graph.setId(graphDeclarationContext.getChild(1).getText());

        return graph;
    }
}
