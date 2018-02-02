package com.squarebit.machinations.models;

import com.squarebit.machinations.SpecGraphReader;
import com.squarebit.machinations.Utils;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MachinationsContextTests {
    @Test
    public void should_construct_pools() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/pool-types.graphml");
        TinkerGraph specs = SpecGraphReader.fromFile(path);
        MachinationsContext context = MachinationsContext.fromSpecs(specs);

        assertThat(context.getVertices().size()).isEqualTo(1);
    }
}
