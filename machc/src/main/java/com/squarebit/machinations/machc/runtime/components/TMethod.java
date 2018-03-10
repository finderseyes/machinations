package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.ast.GMethod;
import com.squarebit.machinations.machc.runtime.Frame;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public final class TMethod {
    public static class Builder {
    }

    private GMethod declaration;
    private TType declaringType;
    private TType returnType;
    private TType[] argumentTypes;
    private String name;
    private Method nativeMethod;
    private Frame frame;

    private TMethod() {

    }

    public GMethod getDeclaration() {
        return declaration;
    }

    public TType getDeclaringType() {
        return declaringType;
    }

    public TType getReturnType() {
        return returnType;
    }

    public TType[] getArgumentTypes() {
        return argumentTypes;
    }

    public String getName() {
        return name;
    }

    public Method getNativeMethod() {
        return nativeMethod;
    }

    public Frame getFrame() {
        return frame;
    }
}
