package com.squarebit.machinations.machc.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * The method data.
 */
public final class GMethod extends GSymbol {
    private List<String> arguments = new ArrayList<>();
    private List<GStatement> statements = new ArrayList<>();

    /**
     * Gets arguments.
     *
     * @return the arguments
     */
    public List<String> getArguments() {
        return arguments;
    }

    /**
     * Adds an argument to the method.
     * @param argument
     */
    public void addArgument(String argument) {
        this.arguments.add(argument);
    }

    /**
     * Gets statements.
     *
     * @return the statements
     */
    public List<GStatement> getStatements() {
        return statements;
    }

    /**
     * Adds a statement to the method.
     *
     * @param statement the statement
     */
    public void addStatement(GStatement statement) {
        this.statements.add(statement);
    }
}
