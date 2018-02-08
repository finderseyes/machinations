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
        }

        {
            AbstractElement element = context.findById("p1");
            assertThat(element).isNotNull();
            assertThat(element instanceof Pool).isTrue();

            Pool pool = (Pool)element;
            assertThat(pool.getName()).isEqualTo("bbb");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.AUTOMATIC);
            assertThat(pool.getFlowMode()).isEqualTo(FlowMode.PUSH_ANY);
        }

        {
            AbstractElement element = context.findById("p2");
            assertThat(element).isNotNull();
            assertThat(element instanceof Pool).isTrue();

            Pool pool = (Pool)element;
            assertThat(pool.getName()).isEqualTo("ccc");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.INTERACTIVE);
            assertThat(pool.getFlowMode()).isEqualTo(FlowMode.PULL_ALL);
        }

        {
            AbstractElement element = context.findById("p3");
            assertThat(element).isNotNull();
            assertThat(element instanceof Pool).isTrue();

            Pool pool = (Pool)element;
            assertThat(pool.getName()).isEqualTo("ddd");
            assertThat(pool.getActivationMode()).isEqualTo(ActivationMode.STARTING_ACTION);
            assertThat(pool.getFlowMode()).isEqualTo(FlowMode.PUSH_ALL);
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
    }

    @Test(expected = Exception.class)
    public void should_not_load_elements_with_same_id_from_yaml_spec() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/duplicated-node-id.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsContextFactory factory = new MachinationsContextFactory();

        MachinationsContext context = factory.fromSpec(spec);
    }
}
