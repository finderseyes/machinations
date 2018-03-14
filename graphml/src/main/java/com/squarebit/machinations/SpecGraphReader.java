package com.squarebit.machinations;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.IOException;

public class SpecGraphReader {
    private static TinkerGraph createGraph() {
        final Configuration conf = getNumberIdManagerConfiguration();
        return TinkerGraph.open(conf);
    }

    private static Configuration getNumberIdManagerConfiguration() {
        final Configuration conf = new BaseConfiguration();
        conf.setProperty(TinkerGraph.GREMLIN_TINKERGRAPH_VERTEX_ID_MANAGER, TinkerGraph.DefaultIdManager.ANY.name());
        conf.setProperty(TinkerGraph.GREMLIN_TINKERGRAPH_EDGE_ID_MANAGER, TinkerGraph.DefaultIdManager.ANY.name());
        conf.setProperty(TinkerGraph.GREMLIN_TINKERGRAPH_VERTEX_PROPERTY_ID_MANAGER, TinkerGraph.DefaultIdManager.LONG.name());
        return conf;
    }

    public static TinkerGraph fromFile(String file) throws IOException {
        final TinkerGraph graph = SpecGraphReader.createGraph();
        graph.io(IoCore.graphml()).readGraph(file);
        return graph;
    }
}
