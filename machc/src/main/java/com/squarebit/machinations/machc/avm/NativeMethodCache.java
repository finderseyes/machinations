package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.runtime.annotations.ConstructorMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class NativeMethodCache {
    private Map<Class, HashMap<MethodSignature, Method>> methodsByType;

    public NativeMethodCache() {
        this.methodsByType = new HashMap<>();
    }

    public Method findMethod(Class clazz, String name, int parameterCount) {
        HashMap<MethodSignature, Method> methods = getNativeMethods(clazz);

        return methods.getOrDefault(new MethodSignature(name, parameterCount), null);
    }

    public Method findConstructor(Class clazz, int parameterCount) {
        HashMap<MethodSignature, Method> methods = getNativeMethods(clazz);

        return methods.getOrDefault(
                new MethodSignature().setConstructor(true).setParameterCount(parameterCount),
                null
        );
    }

    private HashMap<MethodSignature, Method> getNativeMethods(Class clazz) {
        return methodsByType.computeIfAbsent(clazz, c -> {
            HashMap<MethodSignature, Method> methods = new HashMap<>();

            // Constructors.
            Stream.of(clazz.getMethods())
                    .filter(m -> m.getDeclaredAnnotation(ConstructorMethod.class) != null)
                    .forEach(m -> {
                        MethodSignature signature = new MethodSignature()
                                .setConstructor(true).setParameterCount(m.getParameterCount());
                        methods.put(signature, m);
                    });

            return methods;
        });
    }
}
