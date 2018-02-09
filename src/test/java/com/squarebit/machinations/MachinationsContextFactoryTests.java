package com.squarebit.machinations;

import com.squarebit.machinations.models.*;
import com.squarebit.machinations.specs.yaml.YamlSpec;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MachinationsContextFactoryTests {
    @Test
    public void should_load_elements_from_yaml_spec() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/generic-elements.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsContextFactory factory = new MachinationsContextFactory();

        MachinationsContext context = factory.fromSpec(spec);
        assertThat(context).isNotNull();

        {
            AbstractElement element = context.findById("p0");
            assertThat(element).isNotNull();
            assertThat(element instanceof Pool).isTrue();

            Pool pool = (Pool)element;
            assertThat(pool.getName()).isEqualTo("aaa");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.PASSIVE);
            assertThat(pool.getFlowMode()).isEqualTo(FlowMode.AUTOMATIC);

            assertThat(pool.getResourceCount("Mana")).isEqualTo(125);
            assertThat(pool.getResourceCount("Gold")).isEqualTo(30);

            assertThat(pool.getResourceCapacity("Mana")).isEqualTo(200);
            assertThat(pool.getResourceCapacity("Gold")).isEqualTo(-1);

            assertThat(pool.getOutgoingConnections().size()).isEqualTo(3);

            assertThat(pool.getModifiers().size()).isEqualTo(1);
            Modifier modifier = pool.getModifiers().stream().findFirst().get();
            assertThat(modifier.getLabel()).isEqualTo("+2");
            assertThat(modifier.getTarget()).isEqualTo(context.findById("p1"));

            assertThat(pool.getTriggers().size()).isEqualTo(4);

            assertThat(pool.getActivators().size()).isEqualTo(1);
        }

        {
            AbstractElement element = context.findById("p1");
            assertThat(element).isNotNull();
            assertThat(element instanceof Pool).isTrue();

            Pool pool = (Pool)element;
            assertThat(pool.getName()).isEqualTo("bbb");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.AUTOMATIC);
            assertThat(pool.getFlowMode()).isEqualTo(FlowMode.PUSH_ANY);
            assertThat(pool.getTotalResourceCount()).isEqualTo(0);

            assertThat(pool.getIncomingConnections().size()).isEqualTo(1);
        }

        {
            AbstractElement element = context.findById("p2");
            assertThat(element).isNotNull();
            assertThat(element instanceof Pool).isTrue();

            Pool pool = (Pool)element;
            assertThat(pool.getName()).isEqualTo("ccc");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.INTERACTIVE);
            assertThat(pool.getFlowMode()).isEqualTo(FlowMode.PULL_ALL);
            assertThat(pool.getTotalResourceCount()).isEqualTo(100);

            assertThat(pool.getOutgoingConnections().size()).isEqualTo(2);
            assertThat(pool.getIncomingConnections().size()).isEqualTo(1);
        }

        {
            AbstractElement element = context.findById("p3");
            assertThat(element).isNotNull();
            assertThat(element instanceof Pool).isTrue();

            Pool pool = (Pool)element;
            assertThat(pool.getName()).isEqualTo("ddd");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.STARTING_ACTION);
            assertThat(pool.getFlowMode()).isEqualTo(FlowMode.PUSH_ALL);
            assertThat(pool.getTotalResourceCount()).isEqualTo(200);

            assertThat(pool.getIncomingConnections().size()).isEqualTo(2);

            assertThat(pool.getModifiers().size()).isEqualTo(1);
            Modifier modifier = pool.getModifiers().stream().findFirst().get();
            assertThat(modifier.getLabel()).isEqualTo("+3");
            assertThat(modifier.getTarget()).isEqualTo(context.findById("p4"));
        }

        {
            AbstractElement element = context.findById("p4");
            assertThat(element).isNotNull();
            assertThat(element instanceof Pool).isTrue();

            Pool pool = (Pool)element;
            assertThat(pool.getName()).isEqualTo("eee");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.PASSIVE);
            assertThat(pool.getFlowMode()).isEqualTo(FlowMode.PULL_ANY);
        }

        {
            ResourceConnection connection = (ResourceConnection)context.findById("p0_p1");
            assertThat(connection.getFrom()).isEqualTo(context.findById("p0"));
            assertThat(connection.getTo()).isEqualTo(context.findById("p1"));
            assertThat(connection.getLabel()).isEqualTo("");
        }

        {
            ResourceConnection connection = (ResourceConnection)context.findById("p0_p2");
            assertThat(connection.getFrom()).isEqualTo(context.findById("p0"));
            assertThat(connection.getTo()).isEqualTo(context.findById("p2"));
            assertThat(connection.getLabel()).isEqualTo("2D10");
        }

        {
            ResourceConnection connection = (ResourceConnection)context.findById("p2_p3");
            assertThat(connection.getFrom()).isEqualTo(context.findById("p2"));
            assertThat(connection.getTo()).isEqualTo(context.findById("p3"));
            assertThat(connection.getLabel()).isEqualTo("");
        }

        {
            ResourceConnection connection = (ResourceConnection)context.findById("p2_p4");
            assertThat(connection.getFrom()).isEqualTo(context.findById("p2"));
            assertThat(connection.getTo()).isEqualTo(context.findById("p4"));
            assertThat(connection.getLabel()).isEqualTo("5D+10");
        }
    }

    @Test(expected = Exception.class)
    public void should_not_load_elements_with_same_id_from_yaml_spec() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/duplicated-node-id.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsContextFactory factory = new MachinationsContextFactory();

        MachinationsContext context = factory.fromSpec(spec);
    }
}
