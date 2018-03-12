package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.ast.GGraph;
import com.squarebit.machinations.machc.vm.components.TType;

import java.util.HashMap;
import java.util.Map;

/**
 * Information about a TType object during compilation.
 */
public final class TypeInfo extends SymbolInfo implements Scope
{
    public static final String INTERNAL_CONSTRUCTOR_NAME = "$__intctor__$";

    private TType type;
    private UnitInfo unit;
    private TypeInfo baseTypeInfo;
    private MethodInfo internalConstructor;

    private Map<String, FieldInfo> fieldByName = new HashMap<>();
    private Map<String, MethodInfo> methodByName = new HashMap<>();

    /**
     * Instantiates a new Type info.
     *
     * @param unit the unit
     */
    public TypeInfo(UnitInfo unit) {
        this.type = new TType(this);

        this.unit = unit;

        internalConstructor = new MethodInfo(this);
        internalConstructor.setName(TypeInfo.INTERNAL_CONSTRUCTOR_NAME);
        this.addMethod(internalConstructor);
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public TType getType() {
        return type;
    }

    /**
     * Gets base type info.
     *
     * @return the base type info
     */
    public TypeInfo getBaseTypeInfo() {
        return baseTypeInfo;
    }

    /**
     * Sets base type info.
     *
     * @param baseTypeInfo the base type info
     * @return the base type info
     */
    public TypeInfo setBaseTypeInfo(TypeInfo baseTypeInfo) {
        this.baseTypeInfo = baseTypeInfo;
        return this;
    }

    /**
     * Sets declaration.
     *
     * @param graph the graph
     * @return the declaration
     */
    public TypeInfo setDeclaration(GGraph graph) {
        this.declaration = graph;
        return this;
    }

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GGraph getDeclaration() {
        return (GGraph)this.declaration;
    }

    /**
     * Gets internal constructor.
     *
     * @return the internal constructor
     */
    public MethodInfo getInternalConstructor() {
        return internalConstructor;
    }

    /**
     * Add field.
     *
     * @param field the field
     */
    public void addField(FieldInfo field) {
        fieldByName.put(field.name, field);
    }

    /**
     * Find a field.
     *
     * @param name the name
     * @return the field info
     */
    public FieldInfo findField(String name) {
        return fieldByName.get(name);
    }

    /**
     * Adds a method.
     *
     * @param method the method
     */
    public void addMethod(MethodInfo method) {
        methodByName.put(method.name, method);
    }

    /**
     * Find a method by name.
     * @param name the method name.
     * @return method if yet.
     */
    public MethodInfo findMethod(String name) {
        return methodByName.get(name);
    }

    /**
     * Gets the parent scope.
     *
     * @return parent scope.
     */
    @Override
    public Scope getParent() {
        return unit;
    }

    /**
     * Finds a local symbol with given name in this scope.
     *
     * @param name the symbol name
     * @return the symbol or null.
     */
    @Override
    public SymbolInfo findSymbol(String name) {
        return null;
    }
}
