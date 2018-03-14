package com.squarebit.machinations.machc.vm;

/**
 * A local variable declared in or argument passed to a method.
 */
public final class VariableInfo extends SymbolInfo {
    private int index;

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index the index
     * @return the index
     */
    public VariableInfo setIndex(int index) {
        this.index = index;
        return this;
    }
}
