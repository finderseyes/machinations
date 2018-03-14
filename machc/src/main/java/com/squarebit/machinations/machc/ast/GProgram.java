package com.squarebit.machinations.machc.ast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The Mach program.
 */
public final class GProgram extends GObject {
    private Map<String, GUnit> units = new HashMap<>(); // Mapping from unit id to unit instance.

    /**
     * Adds a compilation unit to the program.
     *
     * @param unit the unit
     * @return the g unit
     */
    public GUnit addUnit(GUnit unit) {
        return units.put(unit.getName(), unit);
    }

    /**
     * Gets units declared in this program.
     *
     * @return the units declared in this program.
     */
    public Collection<GUnit> getUnits() {
        return units.values();
    }
}
