package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.instructions.Instruction;
import com.squarebit.machinations.machc.avm.instructions.JumpBlock;

public class NativeToMachineFrame extends InstructionFrame {
    private NativeToMachineInvocation invocation;
    private NativeToMachineInvocation currentInvocation;

    /**
     * Instantiates a new Native to machine frame.
     *
     * @param caller     the caller
     * @param invocation the invocation
     */
    public NativeToMachineFrame(Frame caller, NativeToMachineInvocation invocation) {
        super(caller);
        this.invocation = invocation;
        this.currentInvocation = invocation;
        this.block = new InstructionBlock().setParentScope(getScope(caller));
    }

    /**
     * Gets the number of variable.
     *
     * @return number of local variables.
     */
    @Override
    public int getLocalVariableCount() {
        return 0;
    }

    /**
     * Determines if the frame has more instructions to execute.
     *
     * @return true if the frame has one or more instructions to execute
     */
    @Override
    public boolean hasNext() {
        return this.currentInvocation != null;
    }

    /**
     * Gets the next instruction to execute.
     *
     * @return
     */
    @Override
    public Instruction next() {
        if (this.currentInvocation == null)
            return null;

        NativeToMachineInvocation invocation = this.currentInvocation;
//        this.currentInvocation = invocation.getNext();

        return build(invocation);
    }

    /**
     * Builds the instruction with respect to the invocation.
     * @param invocation invocation.
     * @return invocation.
     */
    private Instruction build(NativeToMachineInvocation invocation) {
        InstructionBlock invocationBlock = new InstructionBlock().setParentScope(this.block);
        return new JumpBlock(invocationBlock);
    }
}
