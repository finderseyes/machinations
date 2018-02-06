package com.squarebit.machinations.models;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class ResourceConnectionNode extends AbstractNode {
    private Edge incomingEdge;
    private Edge outgoingEdge;
    private Vertex vertex;

    public Edge getIncomingEdge() {
        return incomingEdge;
    }

    public ResourceConnectionNode setIncomingEdge(Edge incomingEdge) {
        this.incomingEdge = incomingEdge;
        return this;
    }

    public Edge getOutgoingEdge() {
        return outgoingEdge;
    }

    public ResourceConnectionNode setOutgoingEdge(Edge outgoingEdge) {
        this.outgoingEdge = outgoingEdge;
        return this;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public ResourceConnectionNode setVertex(Vertex vertex) {
        this.vertex = vertex;
        return this;
    }
}
