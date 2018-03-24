package com.squarebit.machinations.machc.ast.statements;

import com.squarebit.machinations.machc.ast.GStatement;

import java.util.ArrayList;
import java.util.List;

public class GVariableDeclaration extends GStatement {
    private List<GVariableDeclarator> declarators;

    public GVariableDeclaration() {
        this.declarators = new ArrayList<>();
    }

    public void add(GVariableDeclarator declarator) {
        this.declarators.add(declarator);
    }

    public List<GVariableDeclarator> getDeclarators() {
        return declarators;
    }

    public GVariableDeclaration setDeclarators(List<GVariableDeclarator> declarators) {
        this.declarators = declarators;
        return this;
    }
}
