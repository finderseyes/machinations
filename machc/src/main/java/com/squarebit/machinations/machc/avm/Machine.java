package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.instructions.*;
import com.squarebit.machinations.machc.avm.runtime.TArray;
import com.squarebit.machinations.machc.avm.runtime.TBoolean;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.TObjectBase;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

public final class Machine {

    private ModuleInfo moduleInfo;

    //////////////////////////////////////////
    // Machine thread dispatcher and running state
    //
    private Dispatcher dispatcher;
    private boolean isRunning = false;
    private Exception lastException = null;
    private final ConcurrentLinkedQueue<Runnable> machineThreadTasks = new ConcurrentLinkedQueue<>();

    //////////////////////////////////////////
    // Call stack and data stack.
    private DataFrame activeDataFrame = null;
    private Frame activeFrame = null;
    private Stack<TObject> dataStack;


    // Expression machine
    private ExpressionMachine expressionMachine;

    // Native method cache.
    private NativeMethodCache nativeMethodCache;

    /**
     * Initializes a new machine instance.
     */
    public Machine(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
        this.dispatcher = Dispatcher.createSingleThreadDispatcher("@avm_main");
        this.activeFrame = null;
        this.dataStack = new Stack<>();
        this.expressionMachine = new ExpressionMachine(this);
        this.nativeMethodCache = new NativeMethodCache();
    }

    /**
     * Starts the machine and its internal loop.
     */
    public void start() {
        isRunning = true;
        dispatcher.dispatch(this::runLoopOnce);
    }

    /**
     * Stops the machine and its internal loop.
     */
    public void shutdown() {
        isRunning = false;
        dispatcher.shutdown();
    }

    /**
     * Gets expression machine.
     *
     * @return the expression machine
     */
    public ExpressionMachine getExpressionMachine() {
        return expressionMachine;
    }

    /**
     *
     * @param moduleInfo
     */
    public void loadModule(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    /**
     * Find type type info.
     *
     * @param name the name
     * @return the type info
     */
    public TypeInfo findType(String name) {
        return this.moduleInfo.findType(name);
    }

    /**
     * Initializes a new instance of given type.
     *
     * @param typeInfo the {@link TypeInfo} of given type
     * @return a {@link CompletableFuture} object holding invocation result
     */
    public CompletableFuture<TObject> newInstance(TypeInfo typeInfo) {
        CompletableFuture<TObject> result = new CompletableFuture<>();
        try {
            TObject instance = typeInfo.allocateInstance();
            return machineInvoke(new MachineInvocationPlan(typeInfo.getInternalInstanceConstructor(), instance))
                    .thenApply(r -> instance);
        }
        catch (Exception ex) {
            result.completeExceptionally(ex);
        }

        return result;
    }

    /**
     *
     * @param machineInvocationPlan
     * @return
     */
    public CompletableFuture<TObject> machineInvoke(MachineInvocationPlan machineInvocationPlan) {
        CompletableFuture<TObject> result = new CompletableFuture<>();

        executeOnMachineThread(() -> {
            NativeToMachineFrame nativeToMachineFrame = pushNativeToMachineFrame(machineInvocationPlan);
            CompletableFuture<TObject> returnFuture = nativeToMachineFrame.getReturnFuture();

            returnFuture.whenComplete((v, ex) -> {
                if (ex != null)
                    result.completeExceptionally(ex);
                else
                    result.complete(v);
            });
        });

        return result;
    }

    /**
     * Invoke a native method.
     * @param method
     * @param instance
     * @param args
     * @return
     */
    private CompletableFuture<TObject> nativeInvoke(Method method, TObject instance, Object ... args) {
        CompletableFuture<TObject> result = new CompletableFuture<>();
        try {
            @SuppressWarnings("unchecked")
            CompletableFuture<TObject> returnFuture = (CompletableFuture<TObject>)(method.invoke(instance, args));

            pushNativeMethodFrame(returnFuture);

            returnFuture.whenComplete((value, exception) -> {
                if (exception != null)
                    result.completeExceptionally(exception);
                else
                    result.complete(value);
            });
        }
        catch (Exception exception) {
            result.completeExceptionally(exception);
        }

        return result;
    }

    /**
     * Execute one execution loop, which executes one instruction at a time.
     */
    private void runLoopOnce() {
        if (!isRunning)
            return;

        // Execute tasks on machine thread.
        while (!machineThreadTasks.isEmpty()) {
            Runnable task = machineThreadTasks.poll();
            task.run();
        }

        executeNextInstruction();

        // Repeat.
        dispatcher.dispatch(this::runLoopOnce);
    }

    /**
     * Execute next instruction in the call stack/active frame.
     */
    private void executeNextInstruction() {
        try {
            Frame frame = this.activeFrame;
            boolean canExecute = false;
            boolean mustWaitForNativeMethod = false;

            while (frame != null && !canExecute && !mustWaitForNativeMethod) {
                mustWaitForNativeMethod = shouldWaitForNativeMethodFrame(frame);

                if (!mustWaitForNativeMethod) {
                    if (!canExecuteNextInstruction(frame)) {
                        Frame exitingFrame = frame;
                        frame = frame.getCaller();
                        this.activeFrame = frame;

                        exitFrame(exitingFrame);
                    }
                    else
                        canExecute = true;
                }
                else {
                    int k = 100;
                }
            }

            // Only execute if there is one executable instruction and no new pending tasks (induced by new frame!).
            if (canExecute && this.machineThreadTasks.isEmpty()) {
                InstructionFrame blockFrame = (InstructionFrame)frame;
                Instruction instruction = blockFrame.next();
                executeInstruction(instruction);
            }
        }
        catch (Exception ex) {
            // Something happens.
            panicReturn(ex);
        }
    }

    /**
     * Determines if we can execute next instruction in given frame.
     * @param frame the frame.
     * @return true or false
     */
    private boolean canExecuteNextInstruction(Frame frame) {
        if (frame instanceof InstructionFrame) {
            InstructionFrame instructionFrame = (InstructionFrame)frame;
            return instructionFrame.hasNext();
        }

        return false;
    }

    private boolean shouldWaitForNativeMethodFrame(Frame frame) {
        return (frame instanceof NativeMethodFrame && !((NativeMethodFrame)frame).getReturnFuture().isDone());
    }

    /**
     * Prepares to enter a new frame.
     * @param frame the new frame
     */
    private void enterFrame(Frame frame) {
        for (int i = 0; i < frame.getLocalVariableCount(); i++)
            dataStack.push(null);
    }

    /**
     * Prepare to exit an existing frame.
     * @param frame the exiting frame.
     */
    private void exitFrame(Frame frame) {
        frame.onExiting(this);

        for (int i = 0; i < frame.getLocalVariableCount(); i++)
            dataStack.pop();

        if (frame instanceof DataFrame) {
            // Move active method frame to nearest method frame.
            this.activeDataFrame = findCallingDataFrame(frame.getCaller());
        }

        frame.onExit(this);
    }

    /**
     * Find the method frame calling this frame.
     *
     * @param frame the frame.
     * @return the method frame or null.
     */
    private DataFrame findCallingDataFrame(Frame frame) {
        while (frame != null && !(frame instanceof DataFrame))
            frame = frame.getCaller();

        if (frame == null)
            return null;
        else
            return (DataFrame)frame;
    }

    /**
     * Execute an instruction.
     * @param instruction instruction to execute
     */
    private void executeInstruction(Instruction instruction) {
        if (instruction instanceof Evaluate)
            executeEvaluate((Evaluate)instruction);
        else if (instruction instanceof PutField)
            executePutField((PutField)instruction);
        else if (instruction instanceof LoadField)
            executeLoadField((LoadField)instruction);
        else if (instruction instanceof Invoke)
            executeInvoke((Invoke)instruction);
        else if (instruction instanceof Return)
            executeReturn((Return)instruction);
        else if (instruction instanceof New)
            executeNew((New)instruction);
        else if (instruction instanceof JumpBlock)
            executeJumpBlock((JumpBlock)instruction);
        else if (instruction instanceof PutConstant)
            executePutConstant((PutConstant)instruction);
        else if (instruction instanceof PutArray)
            executePutArray((PutArray)instruction);
        else if (instruction instanceof LoadArray)
            executeLoadArray((LoadArray)instruction);
        else if (instruction instanceof JumpBlockIf)
            executeJumpBlockIf((JumpBlockIf)instruction);
        else if (instruction instanceof Move)
            executeMove((Move)instruction);
        else if (instruction instanceof JumpIf)
            executeJumpIf((JumpIf)instruction);
        else if (instruction instanceof Jump)
            executeJump((Jump)instruction);
        else if (!(instruction instanceof Label))
            throw new RuntimeException("Unimplemented instruction");
    }

    /**
     * Invoke given method. Must be run by machine thread.
     * @param method
     * @param instance
     * @param args
     * @return
     */
    CompletableFuture<TObject> machInvokeOnMachineThread(MethodInfo method, TObject instance, TObject ... args) {
        MethodFrame methodFrame = pushMethodFrame(method);
        setLocalVariable(0, instance);
        for (int i = 0; i < args.length; i++)
            setLocalVariable(i + 1, args[i]);

        // Push the method first block.
        pushInstructionBlockFrame(method.getInstructionBlock());

        return methodFrame.getReturnFuture();
    }

    /**
     * Push a new frame for a method.
     * @param methodInfo the method
     * @return the method frame.
     */
    private MethodFrame pushMethodFrame(MethodInfo methodInfo) {
        MethodFrame methodFrame = new MethodFrame(this.activeFrame, dataStack.size(), methodInfo);
        enterFrame(methodFrame);

        this.activeDataFrame = methodFrame;
        this.activeFrame = methodFrame;
        return methodFrame;
    }

    /**
     * Push a new frame for an instruction block
     * @param instructionBlock the instruction block
     * @return the instruction block frame
     */
    private InstructionBlockFrame pushInstructionBlockFrame(InstructionBlock instructionBlock) {
        InstructionBlockFrame blockFrame = new InstructionBlockFrame(this.activeFrame, instructionBlock);
        enterFrame(blockFrame);
        this.activeFrame = blockFrame;
        return blockFrame;
    }

    /**
     * Push a new native-to-machine frame.
     * @param machineInvocationPlan
     * @return
     */
    private NativeToMachineFrame pushNativeToMachineFrame(MachineInvocationPlan machineInvocationPlan) {
        NativeToMachineFrame nativeToMachineFrame = new NativeToMachineFrame(
                this.activeFrame, dataStack.size(), machineInvocationPlan
        );
        enterFrame(nativeToMachineFrame);
        this.activeFrame = nativeToMachineFrame;
        this.activeDataFrame = nativeToMachineFrame;
        return nativeToMachineFrame;
    }

    /**
     *
     * @param returnFuture
     * @return
     */
    private NativeMethodFrame pushNativeMethodFrame(CompletableFuture<TObject> returnFuture) {
        NativeMethodFrame nativeMethodFrame = new NativeMethodFrame(this.activeFrame, returnFuture);
        enterFrame(nativeMethodFrame);
        this.activeFrame = nativeMethodFrame;
        return nativeMethodFrame;
    }

    /**
     * Unrolls everything in case of errors.
     * @param exception the cause of the error
     */
    private void panicReturn(Exception exception) {
        Frame frame = this.activeFrame;

        while (frame != null) {
            frame.onPanic(exception);
            frame = frame.getCaller();
        }

        this.isRunning = false;
        this.activeFrame = null;
        this.activeDataFrame = null;
        this.lastException = exception;
    }

    /**
     * Sets a local variable on the stack.
     * @param index variable index.
     * @param value value to set
     */
    void setLocalVariable(int index, TObject value) {
        dataStack.set(this.activeDataFrame.getOffset() + index, value);
    }

    /**
     * Sets local variable.
     *
     * @param variableInfo the variable info
     * @param value        the value
     */
    void setLocalVariable(VariableInfo variableInfo, TObject value) {
        setLocalVariable(variableInfo.getIndex(), value);
    }

    /**
     * Gets a local variable on the stack.
     * @param index the variable index.
     * @return variable value.
     */
    TObject getLocalVariable(int index) {
        return dataStack.get(this.activeDataFrame.getOffset() + index);
    }

    /**
     * Gets local variable.
     *
     * @param variableInfo the variable info
     * @return the local variable
     */
    TObject getLocalVariable(VariableInfo variableInfo) {
        return getLocalVariable(variableInfo.getIndex());
    }

    /**
     * Execute a task on machine thread.
     * @param runnable
     */
    private void executeOnMachineThread(Runnable runnable) {
        if (!isRunning)
            throw new RuntimeException("Machine is not running.", this.lastException);

        machineThreadTasks.add(runnable);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instructions execution

    private void executeEvaluate(Evaluate instruction) {
        expressionMachine.evaluate(instruction.getExpression())
                .thenAccept(result -> setLocalVariable(instruction.getTo().getIndex(), result))
        .exceptionally(ex -> {
            panicReturn((Exception)ex);
            return null;
        });
    }

    private void executePutField(PutField instruction) {
        TObject value = getLocalVariable(instruction.getFrom().getIndex());

        TObject instance = getLocalVariable(instruction.getInstance().getIndex());
        if (instance instanceof TObjectBase) {
            TObjectBase objectBase = (TObjectBase)instance;
            objectBase.setField(instruction.getFieldInfo(), value);
        }
        else
            throw new RuntimeException("The given field does not belong to given type.");
    }

    private void executeLoadField(LoadField instruction) {
        TObject owner = getLocalVariable(instruction.getInstance().getIndex());
        if (owner instanceof TObjectBase) {
            TObjectBase objectBase = (TObjectBase)owner;
            TObject value = objectBase.getField(instruction.getFieldInfo());
            setLocalVariable(instruction.getTo().getIndex(), value);
        }
        else
            throw new RuntimeException("The given field does not belong to given type.");
    }

    private void executeInvoke(Invoke invoke) {
        TObject instance = getLocalVariable(invoke.getInstance().getIndex());

        int parameterCount = invoke.getParameters().length;
        TObject[] parameters = new TObject[parameterCount];
        for (int i = 0; i < parameterCount; i++)
            parameters[i] = getLocalVariable(invoke.getParameters()[i].getIndex());

        CompletableFuture<TObject> returnFuture = machInvokeOnMachineThread(
                invoke.getMethodInfo(),
                instance,
                parameters
        );

        returnFuture.thenAccept(value -> {
            VariableInfo resultVariable = invoke.getTo();
            if (resultVariable != null)
                setLocalVariable(resultVariable.getIndex(), value);
        });
    }

    private void executeReturn(Return instruction) {
        MethodFrame methodFrame = (MethodFrame)findCallingDataFrame(this.activeFrame);
        Frame methodCallerFrame = methodFrame.getCaller();

        if (instruction.getValue() != null) {
            methodFrame.setReturnValue(getLocalVariable(instruction.getValue().getIndex()));
        }

        // Unroll the stack up to current method.
        Frame frame = this.activeFrame;
        while (frame != methodCallerFrame) {
            exitFrame(frame);
            frame = frame.getCaller();
        }

        this.activeFrame = frame;
        this.activeDataFrame = findCallingDataFrame(frame);
    }

    private void executeNew(New instruction) {
        try {
            TypeInfo typeInfo = instruction.getTypeInfo();

            // Allocate the instance first.
            TObject instance = typeInfo.allocateInstance();

            // Try to see if there is a native constructor.
            VariableInfo[] args = instruction.getArgs();
            Object[] argValues = new Object[args.length + 1];
            for (int i = 0; i < args.length; i++)
                argValues[i + 1] = getLocalVariable(args[i].getIndex());
            argValues[0] = this;

            // The 1st argument is the machine.
            Method nativeConstructor = nativeMethodCache.findConstructor(typeInfo.getImplementingClass(), args.length);

            if (nativeConstructor != null) {
                machineInvoke(new MachineInvocationPlan(typeInfo.getInternalInstanceConstructor(), instance))
                        .thenCompose(v -> nativeInvoke(nativeConstructor, instance, argValues))
                        .thenAccept(v -> setLocalVariable(instruction.getTo().getIndex(), instance));
            }
            else {
                // TODO: Try to see if there is a Mac constructor.
                machineInvoke(new MachineInvocationPlan(typeInfo.getInternalInstanceConstructor(), instance))
                        .thenAccept(v0 -> setLocalVariable(instruction.getTo().getIndex(), instance));
            }
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void executeJumpBlock(JumpBlock instruction) {
        pushInstructionBlockFrame(instruction.getBlock());
    }

    private void executePutConstant(PutConstant instruction) {
        setLocalVariable(instruction.getTo().getIndex(), instruction.getValue());
    }

    private void executePutArray(PutArray instruction) {
        TArray array = (TArray)getLocalVariable(instruction.getArray().getIndex());
        TObject value = getLocalVariable(instruction.getFrom().getIndex());
        array.set(instruction.getItemIndex().getValue(), value);
    }

    private void executeLoadArray(LoadArray instruction) {
        TArray array = (TArray)getLocalVariable(instruction.getArray().getIndex());
        TObject value = array.get(instruction.getItemIndex().getValue());
        setLocalVariable(instruction.getTo().getIndex(), value);
    }

    private void executeJumpBlockIf(JumpBlockIf instruction) {
        TBoolean value = (TBoolean)getLocalVariable(instruction.getCondition());
        if (value.getValue())
            pushInstructionBlockFrame(instruction.getWhenTrueBlock());
        else if (instruction.getWhenFalseBlock() != null)
            pushInstructionBlockFrame(instruction.getWhenFalseBlock());
    }

    private void executeMove(Move instruction) {
        TObject fromValue = getLocalVariable(instruction.getFrom());
        setLocalVariable(instruction.getTo(), fromValue);
    }

    private void executeJumpIf(JumpIf instruction) {
        InstructionBlockFrame currentInstructionBlockFrame = (InstructionBlockFrame)this.activeFrame;
        if (instruction.getScope() != currentInstructionBlockFrame.getBlock()) {
            throw new RuntimeException("Invalid jump.");
        }

        TBoolean condition = expressionMachine.evaluateAsBoolean(getLocalVariable(instruction.getCondition()));
        if (condition.getValue())
            currentInstructionBlockFrame.setCounter(instruction.getWhenTrue().getIndex());
        else if (instruction.getWhenFalse() != null)
            currentInstructionBlockFrame.setCounter(instruction.getWhenFalse().getIndex());
    }

    private void executeJump(Jump instruction) {
        InstructionBlockFrame currentInstructionBlockFrame = (InstructionBlockFrame)this.activeFrame;
        if (instruction.getScope() != currentInstructionBlockFrame.getBlock()) {
            throw new RuntimeException("Invalid jump.");
        }

        currentInstructionBlockFrame.setCounter(instruction.getTo().getIndex());
    }
}
