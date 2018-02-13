package com.squarebit.machinations.models;

import com.squarebit.machinations.Utils;
import com.squarebit.machinations.specs.yaml.YamlSpec;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MachinationsContextTests {
    @Test
    public void flow_01() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/flow-01.yaml");
        YamlSpec spec = YamlSpec.fromFile(path);
        MachinationsContextFactory factory = new MachinationsContextFactory();
        MachinationsContext machinations = factory.fromSpec(spec);

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
}
