package com.squarebit.machinations.models;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceContainerTests {
    @Test
    public void should_add_remove() {
        ResourceContainer container = new ResourceContainer();

        assertThat(container.add("", 10)).isEqualTo(10);
        assertThat(container.add(10)).isEqualTo(20);
        assertThat(container.add("G", 10)).isEqualTo(10);

        assertThat(container.size()).isEqualTo(30);

        assertThat(container.remove(5)).isEqualTo(15);
        assertThat(container.remove("G", 5)).isEqualTo(5);
        assertThat(container.size()).isEqualTo(20);
    }

    @Test
    public void should_pull() {
        {
            ResourceContainer container = new ResourceContainer();
            container.add(10);
            ResourceContainer result = container.pull(null, 11, false);
            assertThat(result.size()).isEqualTo(10);
        }

        {
            ResourceContainer container = new ResourceContainer();
            container.add(10);
            ResourceContainer result = container.pull(null, 11, true);
            assertThat(result.size()).isEqualTo(0);
        }

        {
            ResourceContainer container = new ResourceContainer();
            container.add(10);
            ResourceContainer result = container.pull("G", 11, false);
            assertThat(result.size()).isEqualTo(0);
        }
    }
}
