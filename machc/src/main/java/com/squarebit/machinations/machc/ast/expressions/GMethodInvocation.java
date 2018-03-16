package com.squarebit.machinations.machc.ast.expressions;

public class GMethodInvocation extends GExpression {
    private GExpression reference;
    private String methodName;
    private GExpression[] arguments;

    public GMethodInvocation(GExpression reference, String methodName, GExpression[] arguments) {
        this.reference = reference;
        this.methodName = methodName;
        this.arguments = arguments;
    }

    public GExpression getReference() {
        return reference;
    }

    public String getMethodName() {
        return methodName;
    }

    public GExpression[] getArguments() {
        return arguments;
    }
}
