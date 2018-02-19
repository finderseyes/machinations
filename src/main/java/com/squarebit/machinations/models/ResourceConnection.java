package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.ArithmeticExpression;
import com.squarebit.machinations.engine.Expression;
import com.squarebit.machinations.engine.IntNumber;

public class ResourceConnection extends AbstractConnection {
    public static final IntNumber DEFAULT_FLOW_RATE = IntNumber.of(1);

    private AbstractNode from;
    private AbstractNode to;
    private String label = "";
    private Expression flowRateExpression = DEFAULT_FLOW_RATE;
    private String resourceName = null; // if null, any resource.

    public AbstractNode getFrom() {
        return from;
    }

    public ResourceConnection setFrom(AbstractNode from) {
        this.from = from;
        return this;
    }

    public AbstractNode getTo() {
        return to;
    }

    public ResourceConnection setTo(AbstractNode to) {
        this.to = to;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public AbstractConnection setLabel(String label) {
        this.label = label;
        return this;
    }

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
     * Activates the resource connection and gets the required resource set should be passed on it.
     * @return the required resource set
     */
    public ResourceSet activate() {
        if (flowRateExpression instanceof ArithmeticExpression) {
            int amount = ((ArithmeticExpression)flowRateExpression).evaluate();
            return ResourceSet.of(this.resourceName, amount);
        }
        else
            return ResourceSet.empty();
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
