package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.MethodInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

/**
 * Invokes an instance method and stores returned value to a local variable.
 */
public class Invoke extends Instruction {
    private final MethodInfo methodInfo;
    private final VariableInfo instance;
    private final VariableInfo[] parameters;

    private final VariableInfo to;

    /**
     * Instantiates a new Invoke.
     *
     * @param methodInfo    the method info
     * @param instance      the instance on which the method is called
     * @param parameters    the parameters passed to the method
     * @param to            the variable storing returned value
     */
    public Invoke(MethodInfo methodInfo, VariableInfo instance, VariableInfo[] parameters, VariableInfo to) {
        this.methodInfo = methodInfo;
        this.instance = instance;
        this.parameters = parameters;
        this.to = to;
    }

    /**
     * Gets method info.
     *
     * @return the method info
     */
    public MethodInfo getMethodInfo() {
        return methodInfo;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public VariableInfo getInstance() {
        return instance;
    }

    /**
     * Get parameters variable info [ ].
     *
     * @return the variable info [ ]
     */
    public VariableInfo[] getParameters() {
        return parameters;
    }

    /**
     * Gets return store.
     *
     * @return the return store
     */
    public VariableInfo getTo() {
        return to;
    }
}
