package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.Machine;

public abstract class TMachineObject implements TObject {
    private Machine __machine;

    /**
     * Gets the {@link Machine} in which the object is instantiated.
     *
     * @return the machine
     */
    protected Machine getMachine() {
        return __machine;
    }
}
