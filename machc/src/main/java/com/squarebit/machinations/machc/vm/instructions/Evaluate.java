package com.squarebit.machinations.machc.vm.instructions;

import com.squarebit.machinations.machc.vm.Instruction;
import com.squarebit.machinations.machc.vm.InstructionContext;
import com.squarebit.machinations.machc.vm.MachineContext;
import com.squarebit.machinations.machc.vm.TObject;
import com.squarebit.machinations.machc.vm.components.TInteger;
import com.squarebit.machinations.machc.vm.expressions.EvaluationContext;
import com.squarebit.machinations.machc.vm.expressions.Expression;

/**
 * Evaluates an expression and puts result back on the stack.
 */
public class Evaluate extends Instruction {
    private final Expression expression;
    private final int variableCount;

    /**
     * Instantiates a new Evaluate.
     *
     * @param expression    the expression
     * @param variableCount the variable count
     */
    public Evaluate(Expression expression, int variableCount) {
        this.expression = expression;
        this.variableCount = variableCount;
    }

    /**
     * Instantiates a new Evaluate.
     *
     * @param expression the expression
     */
    public Evaluate(Expression expression) {
        this(expression, 0);
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        return expression;
    }

    /**
     * Gets variable count.
     *
     * @return the variable count
     */
    public int getVariableCount() {
        return variableCount;
    }

    /**
     * Execute the instruction given machine instruction context.
     *
     * @param context the instruction context
     */
    @Override
    public void execute(InstructionContext context) {
        TObject[] args = new TObject[variableCount];
        MachineContext machineContext = context.getMachineContext();

        for (int i = variableCount - 1; i >= 0; i--) {
            args[i] = machineContext.popStack();
        }

        EvaluationContext evaluationContext = new EvaluationContext(args);
        TObject result = expression.evaluate(evaluationContext);
        machineContext.pushStack(result);
    }
}
