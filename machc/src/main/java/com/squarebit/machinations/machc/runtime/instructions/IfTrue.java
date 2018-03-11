package com.squarebit.machinations.machc.runtime.instructions;

/**
 * Branching if given condition is true.
 * - Pops current stack as conditional operand.
 * - If popped value evaluates to true, go to given label.
 */
public final class IfTrue {
    private Label label;
}
