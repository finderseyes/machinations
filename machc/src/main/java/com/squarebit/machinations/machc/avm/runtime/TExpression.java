package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;
import com.squarebit.machinations.machc.avm.expressions.Expression;

/**
 * The type T expression.
 */
public class TExpression implements TObject {
    private Expression expression;

    /**
     * Instantiates a new T expression.
     *
     * @param expression the expression
     */
    public TExpression(Expression expression) {
        this.expression = expression;
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
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.EXPRESSION_TYPE;
    }
}
