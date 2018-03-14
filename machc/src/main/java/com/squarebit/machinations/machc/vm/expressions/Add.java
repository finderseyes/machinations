package com.squarebit.machinations.machc.vm.expressions;

import com.squarebit.machinations.machc.vm.TObject;
import com.squarebit.machinations.machc.vm.components.TInteger;
import com.squarebit.machinations.machc.vm.components.TType;
import com.squarebit.machinations.machc.vm.components.Types;

public final class Add extends Expression {
    public final Expression first, second;

    /**
     * Instantiates a new Add.
     *
     * @param first  the first
     * @param second the second
     */
    public Add(Expression first, Expression second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Evalutes the expression under given context.
     *
     * @param context the evaluation context
     * @return result.
     */
    @Override
    public TObject evaluate(EvaluationContext context) {
        TObject firstEval = first.evaluate(context);
        TObject secondEval = second.evaluate(context);

        TType type = coerceType(firstEval.getType(), secondEval.getType());

        if (type == Types.INTEGER_TYPE) {
            TInteger firstInteger = (TInteger)evaluateAs(firstEval, Types.INTEGER_TYPE);
            TInteger secondInteger = (TInteger)evaluateAs(secondEval, Types.INTEGER_TYPE);

            return new TInteger(firstInteger.getValue() + secondInteger.getValue());
        }

        return null;
    }

    private TType coerceType(TType firstType, TType secondType) {
        if (firstType == Types.STRING_TYPE || secondType == Types.STRING_TYPE)
            return Types.STRING_TYPE;
        else if (firstType == Types.FLOAT_TYPE || secondType == Types.FLOAT_TYPE)
            return Types.FLOAT_TYPE;
        else if (firstType == Types.INTEGER_TYPE || secondType == Types.INTEGER_TYPE)
            return Types.INTEGER_TYPE;
        else
            throw new RuntimeException("Cannot coerce types");
    }

    private TObject evaluateAs(TObject value, TType type) {
        if (value.getType() == type)
            return value;

        return null;
    }
}
