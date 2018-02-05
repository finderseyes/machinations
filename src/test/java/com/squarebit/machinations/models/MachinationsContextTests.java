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

        assertThat(context.getNodes().size()).isEqualTo(4);

        {
            Pool pool = (Pool)context.getNode("n0").get();
            assertThat(pool.getName()).isEqualTo("A");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.AUTOMATIC);
            assertThat(pool.getInitialSize()).isEqualTo(10);
        }

        {
            Pool pool = (Pool)context.getNode("n1").get();
            assertThat(pool.getName()).isEqualTo("B");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.INTERACTIVE);
            assertThat(pool.getInitialSize()).isEqualTo(20);
        }

        {
            Pool pool = (Pool)context.getNode("n2").get();
            assertThat(pool.getName()).isEqualTo("C");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.STARTING_ACTION);
            assertThat(pool.getInitialSize()).isEqualTo(30);
        }

        {
            Pool pool = (Pool)context.getNode("n3").get();
            assertThat(pool.getName()).isEqualTo("D");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.PASSIVE);
            assertThat(pool.getInitialSize()).isEqualTo(40);
        }
    }

    @Test
    public void should_construct_resource_connections() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/resource-connection-types.graphml");
        TinkerGraph specs = SpecGraphReader.fromFile(path);
        MachinationsContext context = MachinationsContext.fromSpecs(specs);
    }
}
