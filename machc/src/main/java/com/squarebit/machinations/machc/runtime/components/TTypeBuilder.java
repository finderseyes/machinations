package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.runtime.MachMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * The heper class to build a type.
 */
public final class TTypeBuilder<T extends TObject> {
    private MachMachine machine;
    private String name;
    private TType baseType;
    private Class<T> implementation;
    private boolean valueType;
    private List<TConstructor<T>> constructors = new ArrayList<>();
    private List<TField> fields = new ArrayList<>();
    private List<TMethod> methods = new ArrayList<>();

    /**
     * Gets machine.
     *
     * @return the machine
     */
    public MachMachine getMachine() {
        return machine;
    }

    /**
     * Sets machine.
     *
     * @param machine the machine
     * @return the machine
     */
    public TTypeBuilder<T> setMachine(MachMachine machine) {
        this.machine = machine;
        return this;
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
     * Sets name.
     *
     * @param name the name
     * @return the name
     */
    public TTypeBuilder<T> setName(String name) {
        this.name = name;
        return this;
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
     * Sets base type.
     *
     * @param baseType the base type
     * @return the base type
     */
    public TTypeBuilder<T> setBaseType(TType baseType) {
        this.baseType = baseType;
        return this;
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
     * Sets implementation.
     *
     * @param implementation the implementation
     * @return the implementation
     */
    public TTypeBuilder<T> setImplementation(Class<T> implementation) {
        this.implementation = implementation;
        return this;
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
     * Sets value type.
     *
     * @param valueType the value type
     * @return the value type
     */
    public TTypeBuilder<T> setValueType(boolean valueType) {
        this.valueType = valueType;
        return this;
    }
}
