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
        }
    }
}
