package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.MethodInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

public class Invoke extends Instruction {
    private MethodInfo methodInfo;
    private VariableInfo instance;
    private VariableInfo[] args;

    private VariableInfo result;

    public Invoke(MethodInfo methodInfo, VariableInfo instance, VariableInfo[] args, VariableInfo result) {
        this.methodInfo = methodInfo;
        this.instance = instance;
        this.args = args;
        this.result = result;
    }

    public MethodInfo getMethodInfo() {
        return methodInfo;
    }

    public VariableInfo getInstance() {
        return instance;
    }

    public VariableInfo[] getArgs() {
        return args;
    }

    public VariableInfo getResult() {
        return result;
    }
}
