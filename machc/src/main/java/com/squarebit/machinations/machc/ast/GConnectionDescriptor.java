package com.squarebit.machinations.machc.ast;

import com.squarebit.machinations.machc.ast.expressions.GSetDescriptor;

/**
 * The type G connection.
 */
public class GConnectionDescriptor {
    private GSetDescriptor flow;
    private String from;
    private String to;

    public GSetDescriptor getFlow() {
        return flow;
    }

    public GConnectionDescriptor setFlow(GSetDescriptor flow) {
        this.flow = flow;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public GConnectionDescriptor setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public GConnectionDescriptor setTo(String to) {
        this.to = to;
        return this;
    }
}
