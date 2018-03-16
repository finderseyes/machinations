package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.expressions.Add;
import com.squarebit.machinations.machc.avm.expressions.Constant;
import com.squarebit.machinations.machc.avm.expressions.Expression;
import com.squarebit.machinations.machc.avm.expressions.Variable;
import com.squarebit.machinations.machc.avm.runtime.*;

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
        else if (expression instanceof Variable) {
            Variable variable = (Variable)expression;
            return machine.getLocalVariable(variable.getVariableInfo().getIndex());
        }
        else if (expression instanceof Add) {
            return evaluateAdd((Add)expression);
        }
        else
            throw new RuntimeException("Shall not reach here");
    }

    /**
     * Evaluate constant t object.
     *
     * @param constant the constant
     * @return the t object
     */
    private TObject evaluateConstant(Constant constant) {
        TObject value = constant.getValue();

        if (value instanceof TRandomDice)
            return ((TRandomDice)value).generate();
        else
            return value;
    }

    private TObject evaluateAdd(Add add) {
        TObject first = evaluate(add.getFirst());
        TObject second = evaluate(add.getSecond());

        TypeInfo typeInfo = coerceType(first.getTypeInfo(), second.getTypeInfo());

        if (typeInfo == CoreModule.INTEGER_TYPE) {
            TInteger firstInteger = evaluateAsInteger(first);
            TInteger secondInteger = evaluateAsInteger(second);
            return new TInteger(firstInteger.getValue() + secondInteger.getValue());
        }
        else if (typeInfo == CoreModule.NAN_TYPE)
            return TNaN.INSTANCE;
        else
            return TNaN.INSTANCE;
    }

    private TypeInfo coerceType(TypeInfo firstType, TypeInfo secondType) {
        if (firstType == CoreModule.STRING_TYPE || secondType == CoreModule.STRING_TYPE)
            return CoreModule.STRING_TYPE;
        else if (firstType == CoreModule.NAN_TYPE || secondType == CoreModule.NAN_TYPE)
            return CoreModule.NAN_TYPE;
        else if (firstType == CoreModule.FLOAT_TYPE || secondType == CoreModule.FLOAT_TYPE)
            return CoreModule.FLOAT_TYPE;
        else if (firstType == CoreModule.INTEGER_TYPE || secondType == CoreModule.INTEGER_TYPE)
            return CoreModule.INTEGER_TYPE;
        else
            throw new RuntimeException("Cannot coerce types");
    }

    private TInteger evaluateAsInteger(TObject value) {
        if (value instanceof TInteger)
            return (TInteger)value;
        else if (value instanceof TFloat)
            return new TInteger((int)((TFloat)value).getValue());
        else
            throw new RuntimeException("Cannot convert to integer");
    }
}
