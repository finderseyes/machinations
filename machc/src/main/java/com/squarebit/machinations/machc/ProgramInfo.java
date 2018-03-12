package com.squarebit.machinations.machc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Information about a program.
 */
final class ProgramInfo implements Scope {
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
    public SymbolInfo findSymbol(String name) {
        return unitByName.get(name);
    }
}
