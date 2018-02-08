package com.squarebit.machinations.specs.yaml;

import com.squarebit.machinations.Utils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class YamlSpecsTests {
    @Test
    public void should_load() throws Exception {
        String path = Utils.absoluteResourcePath("graphs/generic-graph.yaml");
        YamlSpec specs = YamlSpec.fromFile(path);
        assertThat(specs).isNotNull();
    }
}
