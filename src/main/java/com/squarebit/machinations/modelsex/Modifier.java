package com.squarebit.machinations.modelsex;

/**
 * (label/node modifier).
 */
public class Modifier {
    private AbstractNode owner;
    private Object target;
    private String label;

    public AbstractNode getOwner() {
        return owner;
    }

    public Modifier setOwner(AbstractNode owner) {
        this.owner = owner;
        return this;
    }

    public Object getTarget() {
        return target;
    }

    public Modifier setTarget(Object target) {
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
}
