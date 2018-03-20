package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.vm.TObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NativeToMachineInvocation {
    private static class Root extends NativeToMachineInvocation {
        private List<Function<TObject, NativeToMachineInvocation>> chain;

        public Root(MethodInfo methodInfo, TObject instance, TObject[] args) {
            super(methodInfo, instance, args);
            this.chain = new ArrayList<>();
        }

        public NativeToMachineInvocation then(Function<TObject, NativeToMachineInvocation> next) {
            chain.add(next);
            return this;
        }
    }

    private MethodInfo methodInfo;
    private TObject instance;
    private TObject[] args;

    private NativeToMachineInvocation(MethodInfo methodInfo, TObject instance, TObject[] args) {
        this.methodInfo = methodInfo;
        this.instance = instance;
        this.args = args;
    }

    public NativeToMachineInvocation then(Function<TObject, NativeToMachineInvocation> next) {
        return this;
    }

    public static NativeToMachineInvocation of(MethodInfo methodInfo, TObject instance, TObject ... args) {
        return new Root(methodInfo, instance, args);
    }
}
