package com.squarebit.machinations.machc.ast.expressions;

public class GMemberAccess extends GExpression implements GAssignmentTarget {
    private GExpression reference;
    private String memberName;

    public GExpression getReference() {
        return reference;
    }

    public GMemberAccess setReference(GExpression reference) {
        this.reference = reference;
        return this;
    }

    public String getMemberName() {
        return memberName;
    }

    public GMemberAccess setMemberName(String memberName) {
        this.memberName = memberName;
        return this;
    }
}
