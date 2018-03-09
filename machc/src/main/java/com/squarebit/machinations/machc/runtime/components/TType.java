package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.runtime.components.annotations.NativeMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Description of a type in the interpreter.
 */
public final class TType<T extends TObject> {
    /**
     * The type builder class.
     * @param <R> type implementing class parameter.
     */
    public static class Builder<R extends TObject> {
        private String name;
        private TType baseType;
        private Class<R> implementation;
        private boolean valueType;

        private List<TField> fields = new ArrayList<>();
        private List<TMethod> methods = new ArrayList<>();

        public Builder<R> setName(String name) {
            this.name = name;
            return this;
        }

        public Builder<R> setBaseType(TType baseType) {
            this.baseType = baseType;
            return this;
        }

        public Builder<R> setImplementation(Class<R> implementation) {
            this.implementation = implementation;
            return this;
        }

        public Builder<R> setValueType(boolean valueType) {
            this.valueType = valueType;
            return this;
        }

        public Builder<R> addField(TField field) {
            this.fields.add(field);
            return this;
        }

        public Builder<R> addMethod(TMethod method) {
            this.methods.add(method);
            return this;
        }

        public Builder<R> scanNativeMethods() {
            Stream.of(implementation.getDeclaredMethods()).forEach(method -> {
                NativeMethod[] annotations = method.getAnnotationsByType(NativeMethod.class);
                if (annotations.length > 0) {
                    TMethod.Builder builder = new TMethod.Builder();
                    builder.setDeclaringType(null).asNativeMethod(method);
                    this.addMethod(builder.build());
                }
            });
            return this;
        }

        public TType<R> build() {
            TType<R> type = new TType<>(
                    name,
                    baseType,
                    implementation,
                    valueType,
                    fields.toArray(new TField[0]),
                    methods.toArray(new TMethod[0])
            );

            Stream.of(type.methods).forEach(m -> m.declaringType = type);

            return type;
        }
    }

    private final String name;
    private final TType baseType;
    private final Class<T> implementation;
    private final boolean valueType;
    private final TField[] fields;
    private final TMethod[] methods;

    private Map<String, TField> fieldByName = new HashMap<>();
    private Map<String, TMethod> methodByName = new HashMap<>();

    /**
     * Instantiates a new type.
     *
     * @param name           the name
     * @param baseType       the base type
     * @param implementation the implementation
     * @param valueType      the value type
     * @param fields         the fields
     * @param methods        the methods
     */
    private TType(final String name,
                  final TType baseType,
                  final Class<T> implementation,
                  final boolean valueType,
                  final TField[] fields,
                  final TMethod[] methods)
    {
        this.name = name;
        this.baseType = baseType;
        this.implementation = implementation;
        this.valueType = valueType;
        this.fields = fields;
        this.methods = methods;

        for (int i = 0; i < fields.length; i++) {
            TField field = fields[i];
            field.fieldTableIndex = i;
            fieldByName.put(field.getName(), field);
        }

        for (int i = 0; i < methods.length; i++) {
            TMethod method = methods[i];
            methodByName.put(method.getName(), method);
        }
    }

    /**
     * Gets the type name.
     *
     * @return the type name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type's base type.
     *
     * @return the base type
     */
    public TType getBaseType() {
        return baseType;
    }

    /**
     * Determines if the type is value type.
     *
     * @return the value type.
     */
    public boolean isValueType() {
        return valueType;
    }

    /**
     * Creates a new instance of the type.
     *
     * @return an instance of this type
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     */
    public T newInstance() throws InstantiationException, IllegalAccessException {
        T instance = implementation.newInstance();

        instance.type = this;
        instance.fieldTable = buildInstanceFieldTable();

        for (TField field: fields) {
            if (field.getType().isValueType()) {
                TObject value = field.getType().newInstance();
                field.set(instance, value);
            }
        }

        return instance;
    }

    /**
     * Gets implementation class.
     *
     * @return the implementation class.
     */
    public Class<T> getImplementation() {
        return implementation;
    }

    /**
     * Get the declared fields.
     *
     * @return the fields declared by this type.
     */
    public TField[] getFields() {
        return fields;
    }

    /**
     * Get the declared methods
     *
     * @return the methods declared by this type.
     */
    public TMethod[] getMethods() {
        return methods;
    }

    /**
     * Get a field with given name.
     *
     * @param name the field name
     * @return the field
     */
    public TField getField(String name) {
        return fieldByName.get(name);
    }

    /**
     * Gets a method with given name.
     *
     * @param name the method name
     * @return the method
     */
    public TMethod getMethod(String name) {
        return methodByName.get(name);
    }

    /**
     * Builds the field table for this type.
     * @return the field table.
     */
    private TObject[] buildInstanceFieldTable() {
        return new TObject[fields.length];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Built-in types
     */

    public static final TType<TObject> OBJECT_TYPE =
            new TType.Builder<>()
                    .setImplementation(TObject.class)
                    .build();

    public static final TType<TVoid> VOID_TYPE =
            new TType.Builder<TVoid>()
                    .setBaseType(OBJECT_TYPE)
                    .setImplementation(TVoid.class)
                    .build();

    public static final TType<TInteger> INTEGER_TYPE = registerTInteger();
    public static final TType<TFloat> FLOAT_TYPE = registerTFloat();
    public static final TType<TBoolean> BOOLEAN_TYPE = registerTBoolean();
    public static final TType<TString> STRING_TYPE = registerTString();

    public static final TType<TGraph> GRAPH_TYPE = registerTGraph();

    /**
     * Determines the type from native type..
     *
     * @param clazz the clazz
     * @return the type
     */
    public static TType fromNative(Class clazz) {
        if (clazz == Void.class || clazz == void.class)
            return VOID_TYPE;
        else if (clazz == Integer.class || clazz == int.class)
            return INTEGER_TYPE;
        else if (clazz == Float.class || clazz == float.class)
            return FLOAT_TYPE;
        else if (clazz == Boolean.class || clazz == boolean.class)
            return BOOLEAN_TYPE;
        else if (TGraph.class.isAssignableFrom(clazz))
            return GRAPH_TYPE;

        throw new RuntimeException("Unknown native class");
    }

    // TInteger type.
    private static TType<TInteger> registerTInteger() {
        TType.Builder<TInteger> builder = new TType.Builder<TInteger>()
                .setBaseType(OBJECT_TYPE)
                .setName(TInteger.class.getSimpleName())
                .setImplementation(TInteger.class)
                .setValueType(true)
                .scanNativeMethods()
                ;

        return builder.build();
    }

    // TFloat type.
    private static TType<TFloat> registerTFloat() {
        TType.Builder<TFloat> builder = new TType.Builder<TFloat>()
                .setBaseType(OBJECT_TYPE)
                .setName(TInteger.class.getSimpleName())
                .setImplementation(TFloat.class)
                .setValueType(true)
                .scanNativeMethods()
                ;

        return builder.build();
    }

    // TBoolean
    private static TType<TBoolean> registerTBoolean() {
        TType.Builder<TBoolean> builder = new TType.Builder<TBoolean>()
                .setBaseType(OBJECT_TYPE)
                .setName(TBoolean.class.getSimpleName())
                .setImplementation(TBoolean.class)
                .setValueType(true)
                .scanNativeMethods()
                ;

        return builder.build();
    }

    // TString
    private static TType<TString> registerTString() {
        TType.Builder<TString> builder = new TType.Builder<TString>()
                .setBaseType(OBJECT_TYPE)
                .setName(TString.class.getSimpleName())
                .setImplementation(TString.class)
                .setValueType(false)
                .scanNativeMethods()
                ;

        return builder.build();
    }

    // TGraph
    private static TType<TGraph> registerTGraph() {
        TType.Builder<TGraph> builder = new TType.Builder<>();
        builder
                .setBaseType(OBJECT_TYPE)
                .setName(TGraph.class.getSimpleName())
                .setImplementation(TGraph.class)
                .setValueType(false)
                .scanNativeMethods()
        ;

        return builder.build();
    }

    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
