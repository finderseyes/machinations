package com.squarebit.machinations.machc.vm;

/**
 *
 */
public class InstructionContext {
    private MachineContext machineContext;

    /**
     * Gets machine context.
     *
     * @return the machine context
     */
    public MachineContext getMachineContext() {
        return machineContext;
    }

    /**
     * Sets machine context.
     *
     * @param machineContext the machine context
     * @return the machine context
     */
    public InstructionContext setMachineContext(MachineContext machineContext) {
        this.machineContext = machineContext;
        return this;
    }
}
