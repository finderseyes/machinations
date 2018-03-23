package com.squarebit.machinations.machc.ast.statements;

import com.squarebit.machinations.machc.ast.GStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * A block of statement.
 */
public class GBlock extends GStatement {
    private List<GStatement> statements = new ArrayList<>();

    /**
     * Add.
     *
     * @param statement the statement
     */
    public void add(GStatement statement) {
        this.statements.add(statement);
    }

    /**
     * Gets statements.
     *
     * @return the statements
     */
    public List<GStatement> getStatements() {
        return statements;
    }
}
