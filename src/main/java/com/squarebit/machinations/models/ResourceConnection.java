package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.Expression;
import com.squarebit.machinations.engine.IntNumber;

public class ResourceConnection extends AbstractConnection {
    public static final IntNumber DEFAULT_FLOW_RATE = IntNumber.of(1);

    private Expression flowRateExpression = DEFAULT_FLOW_RATE;

    /**
     * Gets flow rate expression.
     *
     * @return the flow rate expression
     */
    public Expression getFlowRateExpression() {
        return flowRateExpression;
    }

    /**
     * Sets flow rate expression.
     *
     * @param flowRateExpression the flow rate expression
     * @return the flow rate expression
     */
    public ResourceConnection setFlowRateExpression(Expression flowRateExpression) {
        this.flowRateExpression = flowRateExpression;
        return this;
    }
}
