package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.ast.GGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description of a type in the interpreter.
 */
public final class TType<T extends TObject> {
    /**
     * The type builder class.
     * @param <R> type implementing class parameter.
     */
    public static class Builder<R extends TObject> {
        private TType<R> target;

        private List<TField> fields = new ArrayList<>();
        private List<TMethod> methods = new ArrayList<>();
        private List<TConstructor> constructors = new ArrayList<>();

        /**
         * Instantiates a new builder.
         */
        public Builder() {
            target = new TType<>();
        }

        /**
         * Gets type.
         *
         * @return the type
         */
        public TType<R> getTarget() {
            return target;
        }

        public GGraph getDeclaration() {
            return target.declaration;
        }

        public Builder<R> setDeclaration(GGraph declaration) {
            target.declaration = declaration;
            return this;
        }

        public String getName() {
            return target.name;
        }

        public Builder<R> setName(String name) {
            target.name = name;
            return this;
        }

        public TType getBaseType() {
            return target.baseType;
        }

        public Builder<R> setBaseType(TType baseType) {
            target.baseType = baseType;
            return this;
        }

        public Class<R> getImplementation() {
            return target.implementation;
        }

        public Builder<R> setImplementation(Class<R> implementation) {
            target.implementation = implementation;
            return this;
        }

        public boolean isValueType() {
            return target.valueType;
        }

        public Builder<R> setValueType(boolean valueType) {
            target.valueType = valueType;
            return this;
        }

        public List<TField> getFields() {
            return fields;
        }

        public List<TMethod> getMethods() {
            return methods;
        }

        public List<TConstructor> getConstructors() {
            return constructors;
        }

        public TField.Builder createField() {
            TField.Builder builder = new TField.Builder(this.target);
            builder.addListener(fields::add);
            return builder;
        }

        public Builder<R> addMethod(TMethod method) {
            this.methods.add(method);
            return this;
        }

        public TConstructor.Builder createConstructor() {
            TConstructor.Builder builder = new TConstructor.Builder(this.target);
            builder.addListener(constructors::add);
            return builder;
        }

        public Builder<R> scanNativeMethods() {
//            Stream.of(type.implementation.getDeclaredMethods()).forEach(method -> {
//                NativeMethod[] annotations = method.getAnnotationsByType(NativeMethod.class);
//                if (annotations.length > 0) {
//                    TMethod.Builder builder = new TMethod.Builder();
//                    builder.setDeclaringType(null).asNativeMethod(method);
//                    this.addMethod(builder.build());
//                }
//            });
            return this;
        }

        /**
         * Builds the instance.
         * @return
         */
        public TType<R> build() {
            target.fields = fields.toArray(new TField[0]);
            target.methods = methods.toArray(new TMethod[0]);
            target.constructors = constructors.toArray(new TConstructor[0]);
            return target;
        }
    }

    private GGraph declaration;
    private String name;
    private TType baseType;
    private Class<T> implementation;
    private boolean valueType;
    private TField[] fields;
    private TMethod[] methods;
    private TConstructor[] constructors;

    private Map<String, TField> fieldByName = new HashMap<>();
    private Map<String, TMethod> methodByName = new HashMap<>();

    /**
     * Initializes a new instance of type.
     */
    private TType() {
    }

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GGraph getDeclaration() {
        return declaration;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets base type.
     *
     * @return the base type
     */
    public TType getBaseType() {
        return baseType;
    }

    /**
     * Gets implementation.
     *
     * @return the implementation
     */
    public Class<T> getImplementation() {
        return implementation;
    }

    /**
     * Is value type boolean.
     *
     * @return the boolean
     */
    public boolean isValueType() {
        return valueType;
    }

    /**
     * Get fields t field [ ].
     *
     * @return the t field [ ]
     */
    public TField[] getFields() {
        return fields;
    }

    /**
     * Get methods t method [ ].
     *
     * @return the t method [ ]
     */
    public TMethod[] getMethods() {
        return methods;
    }

    /**
     * Get constructors t constructor [ ].
     *
     * @return the t constructor [ ]
     */
    public TConstructor[] getConstructors() {
        return constructors;
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
