package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.vm.exceptions.TypeNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Information about a program.
 */
public final class ProgramInfo implements Scope {
    private Map<String, UnitInfo> unitByName = new HashMap<>();

    /**
     * Instantiates a new Program info.
     */
    public ProgramInfo() {
    }

    /**
     * Adds a unit.
     * @param unit the unit
     */
    public void addUnit(UnitInfo unit) {
        unitByName.put(unit.name, unit);
    }

    /**
     * Gets units declared in this scope.
     *
     * @return the units
     */
    public Collection<UnitInfo> getUnits() {
        return this.unitByName.values();
    }

    /**
     * Gets the parent scope.
     *
     * @return parent scope.
     */
    @Override
    public Scope getParent() {
        return null;
    }

    /**
     * Finds a local symbol with given name in this scope.
     *
     * @param name the symbol name
     * @return the symbol or null.
     */
    @Override
    public SymbolInfo findLocalSymbol(String name) {
        return unitByName.get(name);
    }

    /**
     * Finds a type with given name.
     *
     * @param name the type name
     * @return the type info
     * @throws TypeNotFoundException when the type with given name is not found
     */
    public List<TypeInfo> findType(String name) throws TypeNotFoundException {
        List<TypeInfo> types = unitByName.values().stream().map(u -> u.findType(name))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (types.size() == 0)
            throw new TypeNotFoundException(name);
        else
            return types;
    }
}
