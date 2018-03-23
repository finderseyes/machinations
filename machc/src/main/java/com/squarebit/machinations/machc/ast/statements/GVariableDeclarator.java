package com.squarebit.machinations.machc.ast.statements;

import com.squarebit.machinations.machc.ast.expressions.GExpression;

public class GVariableDeclarator {
    private String name;
    private GExpression initializer;

    public GVariableDeclarator(String name, GExpression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    public String getName() {
        return name;
    }

    public GExpression getInitializer() {
        return initializer;
    }
}
