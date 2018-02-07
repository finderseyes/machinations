package com.squarebit.machinations.models;

import com.squarebit.machinations.parsers.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntSupplierFactory {
    public Supplier<Integer> fromExpression(String expression) throws Exception {
        if (expression.trim().equals(""))
            return () -> 1;

        CharStream stream = new ANTLRInputStream(expression);
        TokenStream tokens = new CommonTokenStream(new DiceLexer(stream));

        DiceParser parser = new DiceParser(tokens);
        DiceParser.DiceExpressionContext program = parser.diceExpression();

        List<AbstractDiceTerm> terms = IntStream.range(0, program.children.size()).boxed().map(i -> {
            ParseTree c = program.getChild(i);
            ParseTree prev = program.getChild(i - 1);

            if (c instanceof DiceParser.TermContext) {
                int sign = 1;
                if (prev != null) {
                    if (((TerminalNode)prev).getSymbol().getType() == DiceParser.PLUS)
                        sign = 1;
                    else if (((TerminalNode)prev).getSymbol().getType() == DiceParser.MINUS)
                        sign = -1;
                }

                ParseTree firstChild = ((DiceParser.TermContext)c).children.get(0);
                if (firstChild instanceof DiceParser.DiceTermContext) {
                    DiceParser.DiceTermContext term = (DiceParser.DiceTermContext)firstChild;
                    int times = 1;
                    int faces = 6;

                    TerminalNode t0 = (TerminalNode)term.getChild(0);
                    TerminalNode t1 = (TerminalNode)term.getChild(1);
                    TerminalNode t2 = (TerminalNode)term.getChild(2);

                    //
                    if (t0.getText().equals("D")) {
                        // case: D1
                        if (t1 != null) {
                            faces = Integer.parseInt(t1.getText());
                        }
                    }
                    else {
                        times = Integer.parseInt(t0.getText());
                        // case: 1D1
                        if (t2 != null) {

                            faces = Integer.parseInt(t2.getText());
                        }
                    }

                    return new DiceTerm().setTimes(times).setFaces(faces).setSign(sign);
                }
                else if (firstChild instanceof DiceParser.IntegerContext) {
                    DiceParser.IntegerContext term = (DiceParser.IntegerContext)firstChild;
                    int value = Integer.parseInt(term.getText());
                    return new NumericTerm().setValue(value).setSign(sign);
                }
            }

            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return () -> terms.stream().mapToInt(AbstractDiceTerm::evaluate).sum();
    }
}
