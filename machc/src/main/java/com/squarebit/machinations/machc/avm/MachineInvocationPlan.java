package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.runtime.TObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class MachineInvocationPlan {
    private List<Function<TObject, NativeToMachineInvocation>> steps;

    /**
     * Instantiates a new Invocation plan.
     */
    public MachineInvocationPlan() {

    }

    /**
     * Instantiates a new Invocation plan.
     *
     * @param invocation the invocation
     */
    public MachineInvocationPlan(NativeToMachineInvocation invocation) {
        steps = new ArrayList<>();
        steps.add(value -> invocation);
    }

    /**
     * Then invoke invocation plan.
     *
     * @param step the invocation factory
     * @return the invocation plan
     */
    public MachineInvocationPlan thenInvoke(Function<TObject, NativeToMachineInvocation> step) {
        steps.add(step);
        return this;
    }

    /**
     * Gets steps.
     *
     * @return the steps
     */
    public List<Function<TObject, NativeToMachineInvocation>> getSteps() {
        return steps;
    }

    /**
     * Gets step count.
     *
     * @return the step count
     */
    public int getStepCount() {
        return this.steps.size();
    }
}
