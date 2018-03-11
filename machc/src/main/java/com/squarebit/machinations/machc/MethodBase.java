package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.runtime.MachInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class MethodBase {
    List<Variable> variables = new ArrayList<>();
    List<MachInstruction> instructions = new ArrayList<>();
}
