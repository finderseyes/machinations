package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.ArithmeticExpression;
import com.squarebit.machinations.engine.Expression;
import com.squarebit.machinations.engine.IntNumber;

public class ResourceConnection extends AbstractConnection {
    public static final IntNumber DEFAULT_FLOW_RATE = IntNumber.of(1);

    private Expression flowRateExpression = DEFAULT_FLOW_RATE;
    private String resourceName = null; // if null, any resource.

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

    public int getFlowRate() {
        if (flowRateExpression instanceof ArithmeticExpression) {
            return ((ArithmeticExpression)flowRateExpression).evaluate();
        }
        else
            return 0;
    }

    /**
     * Gets resource name.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Sets resource name.
     *
     * @param resourceName the resource name
     * @return the resource name
     */
    public ResourceConnection setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }
}
