package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.runtime.Frame;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public final class TMethod {
    public static class Builder {
        private TType declaringType;
        private TType returnType;
        private TType[] argumentTypes;
        private String name;
        private Method nativeMethod;
        private Frame frame;

        /**
         * Sets declaring returnType.
         *
         * @param declaringType the declaring returnType
         * @return the declaring returnType
         */
        public Builder setDeclaringType(TType declaringType) {
            this.declaringType = declaringType;
            return this;
        }

        /**
         * Sets returnType.
         *
         * @param returnType the returnType
         * @return the returnType
         */
        public Builder setReturnType(TType returnType) {
            this.returnType = returnType;
            return this;
        }

        /**
         * Sets argument types.
         *
         * @param argumentTypes the argument types
         * @return the argument types
         */
        public Builder setArgumentTypes(TType[] argumentTypes) {
            this.argumentTypes = argumentTypes;
            return this;
        }

        /**
         * Sets name.
         *
         * @param name the name
         * @return the name
         */
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets frame.
         *
         * @param frame the frame
         * @return the frame
         */
        public Builder setFrame(Frame frame) {
            this.frame = frame;
            this.nativeMethod = null;
            return this;
        }

        /**
         * As internal method builder.
         *
         * @return the builder
         */
        public Builder asNativeMethod(Method method) {
            this.frame = null;
            this.nativeMethod = method;

            this.name = method.getName();
            this.returnType = TType.fromNative(method.getReturnType());
            this.argumentTypes = Stream.of(method.getParameterTypes())
                    .map(TType::fromNative)
                    .toArray(TType[]::new);
            return this;
        }

        public TMethod build() {
            TMethod method = new TMethod(declaringType, returnType, argumentTypes, name, nativeMethod, frame);
            return method;
        }
    }

    TType declaringType;
    private final TType returnType;
    private final TType[] argumentTypes;
    private final String name;
    private final Method nativeMethod;
    private final Frame frame;

    private TMethod(TType declaringType, TType returnType, TType[] argumentTypes,
                   String name, Method nativeMethod, Frame frame) {
        this.declaringType = declaringType;
        this.returnType = returnType;
        this.argumentTypes = argumentTypes;
        this.name = name;
        this.nativeMethod = nativeMethod;
        this.frame = frame;
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

    public boolean isNativeMethod() {
        return (nativeMethod != null);
    }

    public Method getNativeMethod() {
        return nativeMethod;
    }

    public Frame getFrame() {
        return frame;
    }
}
