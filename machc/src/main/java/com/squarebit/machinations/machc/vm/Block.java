package com.squarebit.machinations.machc.vm;

import java.util.ArrayList;
import java.util.List;

/**
 * A special kind of instruction class to help build method code.
 */
public final class Block extends NoOp implements Scope {
    private Scope parent;
    private MethodInfo method;

    // Set of instruction.
    private List<InstructionBase> instructions = new ArrayList<>();

    /**
     * Instantiates a new Block.
     *
     * @param method the method
     */
    public Block(MethodInfo method) {
        this.parent = this.method = method;
    }

    /**
     * Instantiates a new Block as child of another block.
     *
     * @param parent the parent
     */
    public Block(Block parent) {
        this.method = parent.method;
        this.parent = parent;
    }

    /**
     * Flattens current block.
     *
     * @return the block
     */
    public Block flatten() {
        return null;
    }

    /**
     * Adds an instruction.
     *
     * @param instruction the instruction
     */
    public void add(InstructionBase instruction) {
        instructions.add(instruction);
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public MethodInfo getMethod() {
        return method;
    }

    /**
     * Gets the parent scope.
     *
     * @return parent scope.
     */
    @Override
    public Scope getParent() {
        return parent;
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
