package com.squarebit.machinations.models;

import com.squarebit.machinations.SpecGraphReader;
import com.squarebit.machinations.Utils;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        String path = Utils.absoluteResourcePath("graphs/pool-types.graphml");
        TinkerGraph specs = SpecGraphReader.fromFile(path);
        MachinationsContext context = MachinationsContext.fromSpecs(specs);

        assertThat(context.getConnections().size()).isEqualTo(3);

        {
            Pool n0 = (Pool)context.getNode("n0").get();
            Pool n1 = (Pool)context.getNode("n1").get();

            assertThat(n0.getOutgoingConnections().size()).isEqualTo(1);
            assertThat(n0.getIncomingConnections().size()).isEqualTo(0);

            assertThat(n1.getIncomingConnections().size()).isEqualTo(1);
            assertThat(n1.getOutgoingConnections().size()).isEqualTo(0);

            AbstractConnection c0 = n0.getOutgoingConnections().stream().findFirst().get();
            AbstractConnection c1 = n1.getIncomingConnections().stream().findFirst().get();

            assertThat(c0).isEqualTo(c1);
            assertThat(c0.getFrom()).isEqualTo(n0);
            assertThat(c1.getTo()).isEqualTo(n1);

            assertThat(n0.getTriggers().size()).isEqualTo(1);
            assertThat(n0.getActivators().size()).isEqualTo(1);

            assertThat(n1.getModifiers().size()).isEqualTo(1);
        }

        {
            Pool n2 = (Pool)context.getNode("n2").get();
            Pool n3 = (Pool)context.getNode("n3").get();

            assertThat(n2.getOutgoingConnections().size()).isEqualTo(2);
            assertThat(n2.getIncomingConnections().size()).isEqualTo(0);

            assertThat(n3.getIncomingConnections().size()).isEqualTo(2);
            assertThat(n3.getOutgoingConnections().size()).isEqualTo(0);

            List<AbstractConnection> connections = new ArrayList<>(n2.getOutgoingConnections());
            assertThat(connections.get(0).getFrom()).isEqualTo(n2);
            assertThat(connections.get(1).getFrom()).isEqualTo(n2);

            assertThat(connections.get(0).getTo()).isEqualTo(n3);
            assertThat(connections.get(1).getTo()).isEqualTo(n3);
        }
    }
}
