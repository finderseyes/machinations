package com.squarebit.machinations.machc.ast.expressions;

public final class GLoadField extends GExpression {
    private final GExpression reference;
    private final String fieldName;

    public GLoadField(GExpression reference, String fieldName) {
        this.reference = reference;
        this.fieldName = fieldName;
    }

    public GExpression getReference() {
        return reference;
    }

    public String getFieldName() {
        return fieldName;
    }
}
