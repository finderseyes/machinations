package com.squarebit.machinations.machc.vm.instructions;

import com.squarebit.machinations.machc.vm.Instruction;
import com.squarebit.machinations.machc.vm.InstructionContext;
import com.squarebit.machinations.machc.vm.MachineContext;
import com.squarebit.machinations.machc.vm.TObject;

/**
 * Loads a local variable onto stack.
 */
public final class Load extends Instruction {
    private final int variableIndex;

    /**
     * Instantiates a new Load.
     *
     * @param variableIndex the variable index
     */
    public Load(int variableIndex) {
        this.variableIndex = variableIndex;
    }

    /**
     * Gets variable index.
     *
     * @return the variable index
     */
    public int getVariableIndex() {
        return variableIndex;
    }

    /**
     * Execute the instruction given machine instruction context.
     *
     * @param context the instruction context
     */
    @Override
    public void execute(InstructionContext context) {
        MachineContext machineContext = context.getMachineContext();
        TObject value = machineContext.getLocalVar(variableIndex);
        machineContext.pushStack(value);
    }
}
