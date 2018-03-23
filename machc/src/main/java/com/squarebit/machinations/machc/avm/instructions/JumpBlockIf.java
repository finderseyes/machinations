package com.squarebit.machinations.machc.avm.instructions;

import com.squarebit.machinations.machc.avm.InstructionBlock;
import com.squarebit.machinations.machc.avm.VariableInfo;

public class JumpBlockIf extends Instruction {
    private VariableInfo condition;
    private InstructionBlock whenTrueBlock;
    private InstructionBlock whenFalseBlock;

    /**
     * Instantiates a new Jump block if.
     *
     * @param condition      the condition
     * @param whenTrueBlock  the when true block
     * @param whenFalseBlock the when false block
     */
    public JumpBlockIf(VariableInfo condition, InstructionBlock whenTrueBlock, InstructionBlock whenFalseBlock) {
        this.condition = condition;
        this.whenTrueBlock = whenTrueBlock;
        this.whenFalseBlock = whenFalseBlock;
    }

    /**
     * Gets condition.
     *
     * @return the condition
     */
    public VariableInfo getCondition() {
        return condition;
    }

    /**
     * Gets when true block.
     *
     * @return the when true block
     */
    public InstructionBlock getWhenTrueBlock() {
        return whenTrueBlock;
    }

    /**
     * Gets when false block.
     *
     * @return the when false block
     */
    public InstructionBlock getWhenFalseBlock() {
        return whenFalseBlock;
    }
}
