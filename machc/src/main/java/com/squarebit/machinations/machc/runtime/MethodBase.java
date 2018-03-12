package com.squarebit.machinations.machc.runtime;

import com.squarebit.machinations.machc.vm.VariableInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class MethodBase {
    List<VariableInfo> variables = new ArrayList<>();
    List<MachInstruction> instructions = new ArrayList<>();
}
