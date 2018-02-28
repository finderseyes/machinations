package com.squarebit.machinations.models;

import com.squarebit.machinations.Utils;
import com.squarebit.machinations.specs.yaml.YamlSpec;
import org.assertj.core.data.Offset;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.assertj.core.api.Assertions.assertThat;

public class MachinationsTests {
    @Test
    public void flow_01() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-01.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        {
            machinations.simulateOneTimeStep();

            Pool p0 = (Pool) machinations.findById("p0");
            assertThat(p0.getTotalResourceCount()).isEqualTo(9);

            Pool p1 = (Pool) machinations.findById("p1");
            assertThat(p1.getTotalResourceCount()).isEqualTo(1);

            Pool p2 = (Pool) machinations.findById("p2");
            assertThat(p2.getTotalResourceCount()).isEqualTo(19);

            Pool p3 = (Pool) machinations.findById("p3");
            assertThat(p3.getTotalResourceCount()).isEqualTo(1);

            Pool p4 = (Pool) machinations.findById("p4");
            assertThat(p4.getTotalResourceCount()).isEqualTo(9);

            Pool p5 = (Pool) machinations.findById("p5");
            assertThat(p5.getTotalResourceCount()).isEqualTo(1);

            Pool p6 = (Pool) machinations.findById("p6");
            assertThat(p6.getTotalResourceCount()).isEqualTo(0);
        }

        {
            machinations.simulateOneTimeStep();

            Pool p0 = (Pool) machinations.findById("p0");
            assertThat(p0.getTotalResourceCount()).isEqualTo(8);

            Pool p1 = (Pool) machinations.findById("p1");
            assertThat(p1.getTotalResourceCount()).isEqualTo(2);

            Pool p2 = (Pool) machinations.findById("p2");
            assertThat(p2.getTotalResourceCount()).isEqualTo(18);

            Pool p3 = (Pool) machinations.findById("p3");
            assertThat(p3.getTotalResourceCount()).isEqualTo(2);

            Pool p4 = (Pool) machinations.findById("p4");
            assertThat(p4.getTotalResourceCount()).isEqualTo(8);

            Pool p5 = (Pool) machinations.findById("p5");
            assertThat(p5.getTotalResourceCount()).isEqualTo(1);

            Pool p6 = (Pool) machinations.findById("p6");
            assertThat(p6.getTotalResourceCount()).isEqualTo(1);

            Pool p7 = (Pool) machinations.findById("p7");
            assertThat(p7.getTotalResourceCount()).isEqualTo(8);

            Pool p8 = (Pool) machinations.findById("p8");
            assertThat(p8.getTotalResourceCount()).isEqualTo(2);

            Pool p9 = (Pool) machinations.findById("p9");
            assertThat(p9.getTotalResourceCount()).isEqualTo(0);
        }

        {
            machinations.simulateOneTimeStep();

            Pool p0 = (Pool) machinations.findById("p0");
            assertThat(p0.getTotalResourceCount()).isEqualTo(7);

            Pool p1 = (Pool) machinations.findById("p1");
            assertThat(p1.getTotalResourceCount()).isEqualTo(3);

            Pool p2 = (Pool) machinations.findById("p2");
            assertThat(p2.getTotalResourceCount()).isEqualTo(17);

            Pool p3 = (Pool) machinations.findById("p3");
            assertThat(p3.getTotalResourceCount()).isEqualTo(3);

            Pool p4 = (Pool) machinations.findById("p4");
            assertThat(p4.getTotalResourceCount()).isEqualTo(7);

            Pool p5 = (Pool) machinations.findById("p5");
            assertThat(p5.getTotalResourceCount()).isEqualTo(1);

            Pool p6 = (Pool) machinations.findById("p6");
            assertThat(p6.getTotalResourceCount()).isEqualTo(2);

            Pool p7 = (Pool) machinations.findById("p7");
            assertThat(p7.getTotalResourceCount()).isEqualTo(7);

            Pool p8 = (Pool) machinations.findById("p8");
            assertThat(p8.getTotalResourceCount()).isEqualTo(1);

            Pool p9 = (Pool) machinations.findById("p9");
            assertThat(p9.getTotalResourceCount()).isEqualTo(2);
        }
    }

    @Test
    public void should_support_synchronous_time() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/fig-5-10.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool A = (Pool) machinations.findById("a");
        Pool B = (Pool) machinations.findById("b");
        Pool C = (Pool) machinations.findById("c");
        Pool D = (Pool) machinations.findById("d");

        {
            machinations.simulateOneTimeStep();

            assertThat(A.getResources().size()).isEqualTo(9);
            assertThat(B.getResources().size()).isEqualTo(1);
            assertThat(C.getResources().size()).isEqualTo(0);
            assertThat(D.getResources().size()).isEqualTo(0);
        }

        {
            machinations.simulateOneTimeStep();

            assertThat(A.getResources().size()).isEqualTo(8);
            assertThat(B.getResources().size()).isEqualTo(2);
            assertThat(C.getResources().size()).isEqualTo(0);
            assertThat(D.getResources().size()).isEqualTo(0);
        }

        {
            machinations.simulateOneTimeStep();

            assertThat(A.getResources().size()).isEqualTo(7);
            assertThat(B.getResources().size()).isEqualTo(1);
            assertThat(C.getResources().size()).isEqualTo(1);
            assertThat(D.getResources().size()).isEqualTo(1);
        }
    }

    @Test
    public void should_support_triggers() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-02.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p0 = (Pool) machinations.findById("p0");
        Pool p1 = (Pool) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");
        Pool p3 = (Pool) machinations.findById("p3");
        Pool p4 = (Pool) machinations.findById("p4");
        Pool p5 = (Pool) machinations.findById("p5");
        ResourceConnection e45 = (ResourceConnection) machinations.findById("e45");

        {
            machinations.simulateOneTimeStep();

            assertThat(p2.getResources().size()).isEqualTo(9);
            assertThat(p3.getResources().size()).isEqualTo(1);

            assertThat(p4.getResources().size()).isEqualTo(9);
            assertThat(p5.getResources().size()).isEqualTo(1);
        }
    }

    @Test
    public void should_support_activators() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-03.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p0 = (Pool) machinations.findById("p0");
        Pool p1 = (Pool) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");
        Pool p3 = (Pool) machinations.findById("p3");
        Pool p4 = (Pool) machinations.findById("p4");

        assertThat(p0.isEnabled()).isTrue();
        assertThat(p1.isEnabled()).isTrue();
        assertThat(p2.isEnabled()).isTrue();
        assertThat(p3.isEnabled()).isFalse();
        assertThat(p4.isEnabled()).isTrue();

        {
            machinations.simulateOneTimeStep();

            assertThat(p0.isEnabled()).isTrue();
            assertThat(p1.isEnabled()).isTrue();
            assertThat(p2.isEnabled()).isTrue();
            assertThat(p3.isEnabled()).isTrue();
            assertThat(p4.isEnabled()).isTrue();

            assertThat(p0.getResources().size()).isEqualTo(18);
            assertThat(p1.getResources().size()).isEqualTo(1);
            assertThat(p2.getResources().size()).isEqualTo(1);
            assertThat(p3.getResources().size()).isEqualTo(10);
            assertThat(p4.getResources().size()).isEqualTo(0);
        }

        {
            machinations.simulateOneTimeStep();

            assertThat(p0.isEnabled()).isTrue();
            assertThat(p1.isEnabled()).isTrue();
            assertThat(p2.isEnabled()).isTrue();
            assertThat(p3.isEnabled()).isTrue();
            assertThat(p4.isEnabled()).isTrue();

            assertThat(p0.getResources().size()).isEqualTo(16);
            assertThat(p1.getResources().size()).isEqualTo(2);
            assertThat(p2.getResources().size()).isEqualTo(2);
            assertThat(p3.getResources().size()).isEqualTo(9);
            assertThat(p4.getResources().size()).isEqualTo(1);
        }

        {
            machinations.simulateOneTimeStep();

            assertThat(p0.isEnabled()).isTrue();
            assertThat(p1.isEnabled()).isTrue();
            assertThat(p2.isEnabled()).isTrue();
            assertThat(p3.isEnabled()).isFalse();
            assertThat(p4.isEnabled()).isTrue();

            assertThat(p0.getResources().size()).isEqualTo(14);
            assertThat(p1.getResources().size()).isEqualTo(3);
            assertThat(p2.getResources().size()).isEqualTo(3);
            assertThat(p3.getResources().size()).isEqualTo(8);
            assertThat(p4.getResources().size()).isEqualTo(2);
        }

        {
            machinations.simulateOneTimeStep();

            assertThat(p0.isEnabled()).isTrue();
            assertThat(p1.isEnabled()).isTrue();
            assertThat(p2.isEnabled()).isTrue();
            assertThat(p3.isEnabled()).isFalse();
            assertThat(p4.isEnabled()).isTrue();

            assertThat(p0.getResources().size()).isEqualTo(12);
            assertThat(p1.getResources().size()).isEqualTo(4);
            assertThat(p2.getResources().size()).isEqualTo(4);
            assertThat(p3.getResources().size()).isEqualTo(8);
            assertThat(p4.getResources().size()).isEqualTo(2);
        }
    }

    @Test
    public void should_support_deterministic_gates_with_integers() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-04.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p0 = (Pool) machinations.findById("p0");
        Gate p1 = (Gate) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");
        Pool p3 = (Pool) machinations.findById("p3");

        int totalResources = p0.getResources().size();

        while (p0.getResources().size() > 0) {
            machinations.simulateOneTimeStep();
        }

        assertThat(p0.getResources().size()).isEqualTo(0);
        assertThat(p1.getResources().size()).isEqualTo(0);
        assertThat((float)p2.getResources().size() / totalResources).isCloseTo((float)1/3, Offset.offset(5e-2f));
        assertThat((float)p3.getResources().size() / totalResources).isCloseTo((float)2/3, Offset.offset(5e-2f));
    }

    @Test
    public void should_support_deterministic_gates_with_probable() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-05.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p0 = (Pool) machinations.findById("p0");
        Gate p1 = (Gate) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");
        Pool p3 = (Pool) machinations.findById("p3");

        int totalResources = p0.getResources().size();

        while (p0.getResources().size() > 0) {
            machinations.simulateOneTimeStep();
        }

        assertThat(p0.getResources().size()).isEqualTo(0);
        assertThat(p1.getResources().size()).isEqualTo(0);
        assertThat((float)p2.getResources().size() / totalResources).isCloseTo(.3f, Offset.offset(5e-2f));
        assertThat((float)p3.getResources().size() / totalResources).isCloseTo(.3f, Offset.offset(5e-2f));
    }

    @Test
    public void should_support_deterministic_gates_with_condition() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-06.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p0 = (Pool) machinations.findById("p0");
        Gate p1 = (Gate) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");
        Pool p3 = (Pool) machinations.findById("p3");

        while (p0.getResources().size() > 0) {
            machinations.simulateOneTimeStep();
        }

        assertThat(p0.getResources().size()).isEqualTo(0);
        assertThat(p1.getResources().size()).isEqualTo(0);
        assertThat(p2.getResources().size()).isEqualTo(3);
        assertThat(p3.getResources().size()).isEqualTo(5);
    }

    @Test
    public void should_support_random_gates_with_condition() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-07.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p0 = (Pool) machinations.findById("p0");
        Gate p1 = (Gate) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");
        Pool p3 = (Pool) machinations.findById("p3");

        int totalResources = p0.getResources().size();
        while (p0.getResources().size() > 0) {
            machinations.simulateOneTimeStep();
        }

        assertThat(p0.getResources().size()).isEqualTo(0);
        assertThat(p1.getResources().size()).isEqualTo(0);
        assertThat((float)p2.getResources().size() / totalResources).isCloseTo(.25f, Offset.offset(5e-2f));
        assertThat((float)p3.getResources().size() / totalResources).isCloseTo(.25f, Offset.offset(5e-2f));
    }

    @Test
    public void should_support_random_gates_with_probable() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-08.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p0 = (Pool) machinations.findById("p0");
        Gate p1 = (Gate) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");
        Pool p3 = (Pool) machinations.findById("p3");

        int totalResources = p0.getResources().size();
        while (p0.getResources().size() > 0) {
            machinations.simulateOneTimeStep();
        }

        assertThat(p0.getResources().size()).isEqualTo(0);
        assertThat(p1.getResources().size()).isEqualTo(0);
        assertThat((float)p2.getResources().size() / totalResources).isCloseTo(.40f, Offset.offset(5e-2f));
        assertThat((float)p3.getResources().size() / totalResources).isCloseTo(.30f, Offset.offset(5e-2f));
    }

    @Test
    public void should_support_gates_with_triggers() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-09.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p0 = (Pool) machinations.findById("p0");
        Pool p1 = (Pool) machinations.findById("p1");
        Gate p2 = (Gate) machinations.findById("p2");

        int totalResources = p0.getResources().size();
        while (p0.getResources().size() > 0) {
            machinations.simulateOneTimeStep();
        }

        assertThat(p0.getResources().size()).isEqualTo(0);
        assertThat(p1.getResources().size()).isEqualTo(totalResources);
        assertThat(p2.getResources().size()).isEqualTo(0);
        assertThat(totalResources / (float)machinations.getTime()).isCloseTo(.30f, Offset.offset(5e-2f));
    }

    @Test
    public void should_support_gates_with_conditional_triggers() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-10.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p0 = (Pool) machinations.findById("p0");
        Pool p1 = (Pool) machinations.findById("p1");
        Gate p2 = (Gate) machinations.findById("p2");

        int totalResources = p0.getResources().size();
        while (p0.getResources().size() > 0) {
            machinations.simulateOneTimeStep();
        }

        assertThat(p0.getResources().size()).isEqualTo(0);
        assertThat(p1.getResources().size()).isEqualTo(totalResources);
        assertThat(p2.getResources().size()).isEqualTo(0);
        assertThat(totalResources / (float)machinations.getTime()).isCloseTo(.50f, Offset.offset(1e-1f));
    }

    @Test
    public void should_support_source_and_drain() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-11.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Source p0 = (Source) machinations.findById("p0");
        Pool p1 = (Pool) machinations.findById("p1");
        Drain p2 = (Drain) machinations.findById("p2");

        int count = 10;
        for (int i = 0; i < count; i++)
            machinations.simulateOneTimeStep();

        assertThat(p1.getResources().size()).isEqualTo(1);
    }

    @Test
    public void should_support_converter() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-12.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Source p0 = (Source) machinations.findById("p0");
        Converter p1 = (Converter) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");

        machinations.simulateOneTimeStep();

        assertThat(p1.getResources().size()).isEqualTo(0);
        assertThat(p2.getResources().size()).isEqualTo(2);
    }

    @Test
    public void should_support_flow_of_named_resources() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-13.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Source p0 = (Source) machinations.findById("p0");
        Converter p1 = (Converter) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");
        Pool p3 = (Pool) machinations.findById("p3");
        Pool p4 = (Pool) machinations.findById("p4");

        machinations.simulateOneTimeStep();
        assertThat(p1.getResources().size()).isEqualTo(0);
        assertThat(p2.getResources().size()).isEqualTo(2);
        assertThat(p3.getResources().size()).isEqualTo(10);
        assertThat(p4.getResources().size()).isEqualTo(1);

        machinations.simulateOneTimeStep();
        assertThat(p1.getResources().size()).isEqualTo(0);
        assertThat(p2.getResources().size()).isEqualTo(4);
        assertThat(p3.getResources().size()).isEqualTo(10);
        assertThat(p4.getResources().size()).isEqualTo(1);
    }

    @Test(timeout = 2000)
    public void should_support_end() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-14.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Source p0 = (Source) machinations.findById("p0");
        Pool p1 = (Pool) machinations.findById("p1");

        while (true) {
            if (!machinations.simulateOneTimeStep())
                break;
        }

        assertThat(machinations.getTime()).isEqualTo(6);
    }

    @Test
    public void should_support_value_modifiers() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-15.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p1 = (Pool) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");
        Pool p3 = (Pool) machinations.findById("p3");
        Pool p4 = (Pool) machinations.findById("p4");
        Pool p5 = (Pool) machinations.findById("p5");
        Pool p6 = (Pool) machinations.findById("p6");

        {
            machinations.simulateOneTimeStep();
            assertThat(p1.getResources().size()).isEqualTo(1);
            assertThat(p2.getResources().size()).isEqualTo(1);
            assertThat(p3.getResources().size()).isEqualTo(1);

            assertThat(p4.getResources().size()).isEqualTo(1);
            assertThat(p5.getResources().size()).isEqualTo(1);
            assertThat(p6.evaluate()).isEqualTo(2);
        }

        {
            machinations.simulateOneTimeStep();
            assertThat(p1.getResources().size()).isEqualTo(2);
            assertThat(p2.getResources().size()).isEqualTo(3);
            assertThat(p3.getResources().size()).isEqualTo(2);

            assertThat(p4.getResources().size()).isEqualTo(2);
            assertThat(p5.getResources().size()).isEqualTo(2);
            assertThat(p6.evaluate()).isEqualTo(4);
        }

        {
            machinations.simulateOneTimeStep();
            assertThat(p1.getResources().size()).isEqualTo(3);
            assertThat(p2.getResources().size()).isEqualTo(6);
            assertThat(p3.getResources().size()).isEqualTo(4);
        }
    }

    @Test
    public void should_support_interval_modifiers() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-16.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p1 = (Pool) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");

        {
            machinations.simulateOneTimeStep();
            assertThat(p1.getResources().size()).isEqualTo(1);
            assertThat(p2.getResources().size()).isEqualTo(1);
        }

        {
            machinations.simulateOneTimeStep();
            assertThat(p1.getResources().size()).isEqualTo(1);
            assertThat(p2.getResources().size()).isEqualTo(2);
        }

        {
            machinations.simulateOneTimeStep();
            assertThat(p1.getResources().size()).isEqualTo(1);
            assertThat(p2.getResources().size()).isEqualTo(2);
        }

        {
            machinations.simulateOneTimeStep();
            assertThat(p1.getResources().size()).isEqualTo(1);
            assertThat(p2.getResources().size()).isEqualTo(3);
        }
    }

    @Test
    public void should_support_multiplier_modifiers() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-17.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p1 = (Pool) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");

        {
            machinations.simulateOneTimeStep();
            assertThat(p1.getResources().size()).isEqualTo(1);
            assertThat(p2.getResources().size()).isEqualTo(1);
        }

        {
            machinations.simulateOneTimeStep();
            assertThat(p1.getResources().size()).isEqualTo(1);
            assertThat(p2.getResources().size()).isEqualTo(3);
        }
    }

    @Test
    public void should_support_probability_modifiers() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-18.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsFactory factory = new MachinationsFactory();
        Machinations machinations = factory.fromSpec(spec);

        Pool p1 = (Pool) machinations.findById("p1");
        Pool p2 = (Pool) machinations.findById("p2");

        int times = 1000;
        for (int i = 0; i < times; i++)
            machinations.simulateOneTimeStep();

        assertThat(p2.getResources().size() / (float)times).isCloseTo(0.75f, Offset.offset(5e-2f));

//        {
//            machinations.simulateOneTimeStep();
//            assertThat(p1.getResources().size()).isEqualTo(1);
//            assertThat(p2.getResources().size()).isEqualTo(1);
//        }
//
//        {
//            machinations.simulateOneTimeStep();
//            assertThat(p1.getResources().size()).isEqualTo(1);
//            assertThat(p2.getResources().size()).isEqualTo(3);
//        }
    }
}
