package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.runtime.MachInstruction;
import com.squarebit.machinations.machc.runtime.instructions.Label;

import java.util.LinkedList;
import java.util.List;

/**
 * A special kind of instruction class to help build method code.
 */
public class Block extends Instruction implements Scope {
    private MethodInfo method;

    private List<Instruction> instructions = new LinkedList<>();
    private Label start;
    private int id;
    private static int blockIdGenerator = 0;

    /**
     * Instantiates a new Block.
     *
     * @param method the method
     */
    public Block(MethodInfo method) {
        this.method = method;
    }

    /**
     * Instantiates a new Block.
     *
     * @param name the name
     */
    public Block(String name) {
        this.id = blockIdGenerator++;

        name = (name == null) ? String.format("__block__%d", id) : name;
        start = new Label(name);
        instructions.add(start);
    }

    public MachInstruction add(MachInstruction instruction) {
        instructions.add(instruction);
        return instruction;
    }

    /**
     * Add a child block at current position with given name.
     *
     * @param name the name
     * @return the block
     */
    public Block addBlock(String name) {
        Block block = new Block(name);
        instructions.add(block);
        return block;
    }

    /**
     * Flatten the block to a list of executable instructions.
     * @return a flat instruction list.
     */
    public List<MachInstruction> flatten() {
        return null;
    }

    /**
     * Gets the parent scope.
     *
     * @return parent scope.
     */
    @Override
    public Scope getParent() {
        return method;
    }

    /**
     * Finds a local symbol with given name in this scope.
     *
     * @param name the symbol name
     * @return the symbol or null.
     */
    @Override
    public SymbolInfo findSymbol(String name) {
        return null;
    }
}
