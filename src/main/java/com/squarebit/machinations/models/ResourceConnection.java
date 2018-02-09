package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.ArithmeticExpression;
import com.squarebit.machinations.engine.IntNumber;

public class ResourceConnection extends AbstractConnection {
    public static final IntNumber DEFAULT_FLOW_RATE = IntNumber.of(1);

    private ArithmeticExpression flowRateExpression = DEFAULT_FLOW_RATE;

    /**
     * Gets flow rate expression.
     *
     * @return the flow rate expression
     */
    public ArithmeticExpression getFlowRateExpression() {
        return flowRateExpression;
    }

    /**
     * Sets flow rate expression.
     *
     * @param flowRateExpression the flow rate expression
     * @return the flow rate expression
     */
    public ResourceConnection setFlowRateExpression(ArithmeticExpression flowRateExpression) {
        this.flowRateExpression = flowRateExpression;
        return this;
    }
}
