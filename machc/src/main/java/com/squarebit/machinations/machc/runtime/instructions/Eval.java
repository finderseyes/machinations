package com.squarebit.machinations.machc.runtime.instructions;

import com.squarebit.machinations.machc.runtime.FrameActivation;
import com.squarebit.machinations.machc.runtime.Instruction;
import com.squarebit.machinations.machc.runtime.Variable;
import com.squarebit.machinations.machc.runtime.components.TObject;
import com.squarebit.machinations.machc.runtime.components.TType;
import com.squarebit.machinations.machc.runtime.expressions.TExpression;

/**
 * Evaluates an expression and stores its result to a variable.
 */
public final class Eval extends Instruction {
    private final TExpression expression;
    private final Variable to;

    /**
     * Instantiates a new Eval.
     *
     * @param from the expression
     * @param to   the to
     */
    public Eval(TExpression from, Variable to) {
        this.expression = from;
        this.to = to;
    }

    /**
     * Execute the current instruction.
     */
    @Override
    public void execute() {
        FrameActivation activation = this.getFrame().currentActivation();
        if (to != null) {
            TObject value = expression.evalTo(to.getType(), activation);
            to.set(activation, value);
        }
        else {
            expression.evalTo(TType.OBJECT_TYPE, activation);
        }
    }
}
