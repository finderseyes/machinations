package com.squarebit.machinations.machc.avm;


import com.squarebit.machinations.machc.avm.runtime.TObject;

public class NativeToMachineInvocation {
    private MethodInfo methodInfo;
    private TObject instance;
    private TObject[] args;

    public NativeToMachineInvocation(MethodInfo methodInfo, TObject instance, TObject ... args) {
        this.methodInfo = methodInfo;
        this.instance = instance;
        this.args = args;
    }

    public MethodInfo getMethodInfo() {
        return methodInfo;
    }

    public TObject getInstance() {
        return instance;
    }

    public TObject[] getArgs() {
        return args;
    }
}
