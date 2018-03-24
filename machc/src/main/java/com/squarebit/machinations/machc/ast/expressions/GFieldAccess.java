package com.squarebit.machinations.machc.ast.expressions;

public class GFieldAccess extends GExpression implements GAssignmentTarget {
    private GExpression reference;
    private String fieldName;
}
