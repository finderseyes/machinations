package com.squarebit.machinations.machc.vm.expressions;

import com.squarebit.machinations.machc.vm.TObject;

public final class ObjectRef extends Expression {
    private final TObject ref;

    /**
     * Instantiates a new Object ref.
     *
     * @param ref the ref
     */
    public ObjectRef(TObject ref) {
        this.ref = ref;
    }

    /**
     * Gets ref.
     *
     * @return the ref
     */
    public TObject getRef() {
        return ref;
    }

    /**
     * Evalutes the expression under given context.
     *
     * @param context the evaluation context
     * @return result.
     */
    @Override
    public TObject evaluate(EvaluationContext context) {
        return ref;
    }
}
