package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.MethodInfo;
import com.squarebit.machinations.machc.avm.VariableInfo;

public class Invoke extends Instruction {
    private MethodInfo methodInfo;
    private VariableInfo instance;
    private VariableInfo[] args;

    private VariableInfo result;
}
