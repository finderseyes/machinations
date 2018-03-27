package com.squarebit.machinations.machc.ast;

import com.squarebit.machinations.machc.ast.expressions.GExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * The method data.
 */
public final class GMethod extends GSymbol {
    private List<String> arguments = new ArrayList<>();
    private List<GStatement> statements = new ArrayList<>();
    private GMethodModifier modifier;
    private GExpression interactiveCondition;

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

    /**
     * Gets modifier.
     *
     * @return the modifier
     */
    public GMethodModifier getModifier() {
        return modifier;
    }

    /**
     * Sets modifier.
     *
     * @param modifier the modifier
     * @return the modifier
     */
    public GMethod setModifier(GMethodModifier modifier) {
        this.modifier = modifier;
        return this;
    }

    /**
     * Gets interactive condition.
     *
     * @return the interactive condition
     */
    public GExpression getInteractiveCondition() {
        return interactiveCondition;
    }

    /**
     * Sets interactive condition.
     *
     * @param interactiveCondition the interactive condition
     * @return the interactive condition
     */
    public GMethod setInteractiveCondition(GExpression interactiveCondition) {
        this.interactiveCondition = interactiveCondition;
        return this;
    }
}
