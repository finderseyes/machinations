package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.runtime.Instruction;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class MethodBase {
    List<Variable> variables = new ArrayList<>();
    List<Instruction> instructions = new ArrayList<>();
}
