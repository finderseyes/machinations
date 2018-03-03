package com.squarebit.machinations.machc.ast;

import java.util.HashMap;
import java.util.Map;

/**
 * The Mach program.
 */
public final class GProgram extends GObject {
    private Map<String, GUnit> units = new HashMap<>(); // Mapping from unit id to unit instance.
}
