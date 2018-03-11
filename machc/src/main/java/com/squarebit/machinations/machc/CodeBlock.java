package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.runtime.Instruction;
import com.squarebit.machinations.machc.runtime.instructions.Label;

import java.util.LinkedList;
import java.util.List;

/**
 * A helper class to build method code.
 */
public class CodeBlock extends Label {
    private List<Instruction> instructions = new LinkedList<>();

    public Instruction add(Instruction instruction) {
        instructions.add(instruction);
        return instruction;
    }
}
