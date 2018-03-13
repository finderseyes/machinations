package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.ast.GUnit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Information about a unit.
 */
public final class UnitInfo extends SymbolInfo implements Scope {
    private ProgramInfo program;
    private Map<String, TypeInfo> typeByName = new HashMap<>();

    /**
     * Instantiates a new Unit info.
     *
     * @param program the program
     */
    public UnitInfo(ProgramInfo program) {
        this.program = program;
    }

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GUnit getDeclaration() {
        return (GUnit)declaration;
    }

    /**
     * Sets declaration.
     *
     * @param unit the declaration
     * @return the declaration
     */
    public UnitInfo setDeclaration(GUnit unit) {
        this.declaration = unit;
        return this;
    }

    /**
     * Adds a type to the unit.
     *
     * @param type the type
     */
    public void addType(TypeInfo type) {
        typeByName.put(type.name, type);
    }

    /**
     * Gets types declared in this scope.
     *
     * @return the types
     */
    public Collection<TypeInfo> getTypes() {
        return typeByName.values();
    }

    /**
     * Gets the parent scope.
     *
     * @return parent scope.
     */
    @Override
    public Scope getParent() {
        return program;
    }

    /**
     * Finds a local symbol with given name in this scope.
     *
     * @param name the symbol name
     * @return the symbol or null.
     */
    @Override
    public SymbolInfo findLocalSymbol(String name) {
        return findType(name);
    }

    /**
     * Finds a type declared in this unit with given name.
     *
     * @param name the name
     * @return the type info
     */
    public TypeInfo findType(String name) {
        return typeByName.getOrDefault(name, null);
    }
}
