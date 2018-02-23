package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.ArithmeticExpression;
import com.squarebit.machinations.engine.IntegerExpression;

public class Modifier {
    private AbstractNode owner;
    private AbstractElement target;

    private String label;
    private ArithmeticExpression rateExpression;

    public AbstractNode getOwner() {
        return owner;
    }

    public Modifier setOwner(AbstractNode owner) {
        this.owner = owner;
        return this;
    }

    public AbstractElement getTarget() {
        return target;
    }

    public Modifier setTarget(AbstractElement target) {
        this.target = target;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Modifier setLabel(String label) {
        this.label = label;
        return this;
    }

    public ArithmeticExpression getRateExpression() {
        return rateExpression;
    }

    public Modifier setRateExpression(ArithmeticExpression rateExpression) {
        this.rateExpression = rateExpression;
        return this;
    }
}
