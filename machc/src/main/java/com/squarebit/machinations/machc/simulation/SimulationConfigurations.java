package com.squarebit.machinations.machc.simulation;

import com.squarebit.machinations.machc.avm.ModuleInfo;
import com.squarebit.machinations.machc.avm.TypeInfo;

/**
 * Configuration regarding to a simulation session.
 */
public final class SimulationConfigurations {
    private ModuleInfo moduleInfo;
    private TypeInfo mainGraphType;

    /**
     * Gets module info.
     *
     * @return the module info
     */
    public ModuleInfo getModuleInfo() {
        return moduleInfo;
    }

    /**
     * Sets module info.
     *
     * @param moduleInfo the module info
     * @return the module info
     */
    public SimulationConfigurations setModuleInfo(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
        return this;
    }

    /**
     * Gets main graph type.
     *
     * @return the main graph type
     */
    public TypeInfo getMainGraphType() {
        return mainGraphType;
    }

    /**
     * Sets main graph type.
     *
     * @param mainGraphType the main graph type
     * @return the main graph type
     */
    public SimulationConfigurations setMainGraphType(TypeInfo mainGraphType) {
        this.mainGraphType = mainGraphType;
        return this;
    }
}
