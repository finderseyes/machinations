package com.squarebit.machinations.modelsex;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public class ConnectionLabel extends AbstractNode {
    private Edge incomingEdge;
    private Edge outgoingEdge;
    private Vertex vertex;

    public Edge getIncomingEdge() {
        return incomingEdge;
    }

    public ConnectionLabel setIncomingEdge(Edge incomingEdge) {
        this.incomingEdge = incomingEdge;
        return this;
    }

    public Edge getOutgoingEdge() {
        return outgoingEdge;
    }

    public ConnectionLabel setOutgoingEdge(Edge outgoingEdge) {
        this.outgoingEdge = outgoingEdge;
        return this;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public ConnectionLabel setVertex(Vertex vertex) {
        this.vertex = vertex;
        return this;
    }
}
