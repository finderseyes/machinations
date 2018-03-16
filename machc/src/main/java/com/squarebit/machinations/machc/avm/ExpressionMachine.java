package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.expressions.Constant;
import com.squarebit.machinations.machc.avm.expressions.Expression;
import com.squarebit.machinations.machc.avm.runtime.TInteger;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.TRandomDice;

/**
 * In charge of expression evaluation.
 */
final class ExpressionMachine {
    private final Machine machine;

    /**
     * Instantiates a new Expression machine.
     *
     * @param machine the machine
     */
    public ExpressionMachine(Machine machine) {
        this.machine = machine;
    }

    /**
     * Evaluates an expression.
     *
     * @param expression the expression
     * @return the expression
     */
    public TObject evaluate(Expression expression) {
        if (expression instanceof Constant)
            return evaluateConstant((Constant)expression);
        else
            throw new RuntimeException("Shall not reach here");
    }

    /**
     * Evaluate constant t object.
     *
     * @param constant the constant
     * @return the t object
     */
    public TObject evaluateConstant(Constant constant) {
        TObject value = constant.getValue();

        if (value instanceof TRandomDice)
            return ((TRandomDice)value).generate();
        else
            return value;
    }
}
