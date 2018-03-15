package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.VariableInfo;
import com.squarebit.machinations.machc.avm.expressions.Expression;

public class Evaluate extends Instruction {
    private Expression expression;
    private VariableInfo[] arguments;
    private VariableInfo result;
}
