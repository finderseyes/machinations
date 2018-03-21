package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.instructions.Instruction;
import com.squarebit.machinations.machc.avm.instructions.Invoke;
import com.squarebit.machinations.machc.avm.instructions.JumpBlock;
import com.squarebit.machinations.machc.avm.instructions.PutConstant;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.TVoid;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class NativeToMachineFrame extends InstructionFrame implements DataFrame {
    private int offset;
    private MachineInvocationPlan machineInvocationPlan;
    private int counter;

    private VariableInfo returnValueStore;

    private TObject returnValue = TVoid.INSTANCE;
    private CompletableFuture<TObject> returnFuture;

    /**
     * Instantiates a new Native to machine frame.
     *
     * @param caller                    the caller
     * @param machineInvocationPlan     machine invocation plan
     */
    public NativeToMachineFrame(Frame caller, int offset, MachineInvocationPlan machineInvocationPlan) {
        super(caller);
        this.offset = offset;
        this.machineInvocationPlan = machineInvocationPlan;
        this.counter = 0;

        this.block = new InstructionBlock().setParentScope(getScope(caller));
        this.returnValueStore = this.block.createTempVar();

        this.returnFuture = new CompletableFuture<>();
    }

    /**
     * Gets frame data offset.
     *
     * @return the frame data offset
     */
    @Override
    public int getOffset() {
        return this.offset;
    }

    /**
     * Gets return future.
     *
     * @return the return future
     */
    public CompletableFuture<TObject> getReturnFuture() {
        return returnFuture;
    }

    /**
     * Gets the number of variable.
     *
     * @return number of local variables.
     */
    @Override
    public int getLocalVariableCount() {
        return 1;
    }

    /**
     * Determines if the frame has more instructions to execute.
     *
     * @return true if the frame has one or more instructions to execute
     */
    @Override
    public boolean hasNext() {
        return this.counter < machineInvocationPlan.getStepCount();
    }

    /**
     * Gets the next instruction to execute.
     *
     * @return
     */
    @Override
    public Instruction next() {
        if (!hasNext()) return null;

        Function<TObject, NativeToMachineInvocation> step = machineInvocationPlan.getSteps().get(counter);
        NativeToMachineInvocation invocation = step.apply(null);

        counter++;
        return build(invocation);
    }

    /**
     * Builds the instruction with respect to the invocation.
     * @param invocation invocation.
     * @return invocation.
     */
    private Instruction build(NativeToMachineInvocation invocation) {
        InstructionBlock invocationBlock = new InstructionBlock().setParentScope(this.block);

        VariableInfo instanceVar = invocationBlock.createTempVar();
        invocationBlock.emit(new PutConstant(invocation.getInstance(), instanceVar));

        TObject[] args = invocation.getArgs();
        VariableInfo[] argVars = new VariableInfo[args.length];
        for (int i = 0; i < args.length; i++)
            invocationBlock.emit(new PutConstant(args[i], argVars[i]));

        invocationBlock.emit(new Invoke(invocation.getMethodInfo(), instanceVar, argVars, returnValueStore));

        return new JumpBlock(invocationBlock);
    }

    /**
     * Called back when the frame is exiting, data stack is not popped.
     */
    @Override
    public void onExiting(Machine machine) {
        this.returnValue = machine.getLocalVariable(returnValueStore.getIndex());
    }

    /**
     * Called back when the frame is exited, data stack is popped.
     */
    @Override
    public void onExit(Machine machine) {
        returnFuture.complete(returnValue);
    }

    /**
     * On panic.
     *
     * @param exception the exception
     */
    @Override
    public void onPanic(Exception exception) {
        returnFuture.completeExceptionally(exception);
    }
}
