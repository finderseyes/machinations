package com.squarebit.machinations.machc.vm;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Mach machine.
 */
public final class MachMachine {
    private final ProgramInfo programInfo;
    private final MachineContext machineContext = new MachineContext();
    private final Dispatcher dispatcher = Dispatcher.createSingleThreadDispatcher("@mach_machine");
    private boolean running = false;
    private final ConcurrentLinkedQueue<Runnable> machineThreadTasks = new ConcurrentLinkedQueue<>();

    /**
     * Instantiates a new Mach machine.
     *
     * @param programInfo the program info
     */
    public MachMachine(ProgramInfo programInfo) {
        this.programInfo = programInfo;
    }

    /**
     * Starts the machine and its internal loop.
     */
    public void start() {
        running = true;
        dispatcher.dispatch(this::runLoopOnce);
    }

    /**
     * Stops the machine and its internal loop.
     */
    public void shutdown() {
        running = false;
        dispatcher.shutdown();
    }

    /**
     * Execute one execution loop, which executes one instruction at a time.
     */
    private void runLoopOnce() {
        if (!running)
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

    private void executeOnMachineThread(Runnable runnable) {
        machineThreadTasks.add(runnable);
    }

    /**
     * Creates a new instance of given type.
     *
     * @param type the type
     * @return the completable future
     * @throws Exception the exception
     */
    public CompletableFuture<TObject> newInstance(TypeInfo type) throws Exception {
        TObject instance = (TObject)type.getImplementation().newInstance();
        instance.__fields__ = new TObject[type.getFields().size()];

        return nativeToMachInvoke(this.machineContext, type.getInternalConstructor(), instance);
    }

    /**
     * Invoke a Mach method from native.
     * @param context
     * @param methodInfo
     * @param instance
     * @param args
     * @return
     */
    public CompletableFuture<TObject> nativeToMachInvoke(MachineContext context, MethodInfo methodInfo,
                                                         TObject instance, TObject ... args)
    {
        Frame currentFrame = context.getCurrentFrame();
        CompletableFuture<TObject> frameReturn = (currentFrame != null) ? currentFrame.getFrameReturn() : null;

        if (frameReturn != null)
            return frameReturn.thenCompose(v -> nativeToMachInvoke(methodInfo, instance, args));
        else
            return nativeToMachInvoke(methodInfo, instance, args);
    }

    /**
     * Invoke a given mach method under current context.
     * @param method
     * @param instance
     * @param args
     * @return
     */
    private CompletableFuture<TObject> nativeToMachInvoke(MethodInfo method, TObject instance, TObject ... args)
    {
        CompletableFuture<TObject> result = new CompletableFuture<>();

        executeOnMachineThread(() -> {
            CompletableFuture<TObject> internalResult = machInvokeOnMachineThread(method, instance, args);
            internalResult.thenAccept(result::complete);
        });

        return result;
    }

    /**
     * Invoke given method. Must be run by machine thread.
     * @param method
     * @param instance
     * @param args
     * @return
     */
    private CompletableFuture<TObject> machInvokeOnMachineThread(MethodInfo method, TObject instance, TObject ... args) {
        machineContext.pushStack(instance);
        for (TObject arg: args) {
            machineContext.pushStack(arg);
        }

        Frame frame = machineContext.pushFrame(method);
        return frame.getFrameReturn();
    }

    /**
     * Execute next instruction.
     */
    private void executeNextInstruction() {
        Frame frame = null;
        InstructionBase instruction = null;

        while (!machineContext.isCallStackEmpty() && instruction == null) {
            frame = machineContext.getCurrentFrame();
            MethodInfo method = frame.getMethod();
            List<InstructionBase> instructions = method.getCode().getInstructions();

            if (frame.getInstructionCounter() < instructions.size()) {
                instruction = instructions.get(frame.getInstructionCounter());
            }
            else {
                machineContext.popFrame();
            }
        }

        if (frame != null && instruction != null) {
            executeInstruction(instruction);
            frame.setInstructionCounter(frame.getInstructionCounter() + 1);
        }
    }

    private void executeInstruction(InstructionBase instruction) {
        instruction.execute(new InstructionContext().setMachineContext(machineContext));
    }
}
