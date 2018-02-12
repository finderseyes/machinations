package com.squarebit.machinations.models;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceSetTests {
    @Test
    public void should_add_remove() {
        ResourceSet container = new ResourceSet();

        assertThat(container.add("", 10)).isEqualTo(10);
        assertThat(container.add(10)).isEqualTo(20);
        assertThat(container.add("G", 10)).isEqualTo(10);

        assertThat(container.size()).isEqualTo(30);

        assertThat(container.remove(5)).isEqualTo(15);
        assertThat(container.remove("G", 5)).isEqualTo(5);
        assertThat(container.size()).isEqualTo(20);
    }

    @Test
    public void should_commit_and_discard() {
        ResourceSet container = new ResourceSet();

        container.add(10);
        assertThat(container.deltaSize()).isEqualTo(10);

        container.add("G", 5);
        assertThat(container.deltaSize()).isEqualTo(15);

        container.discard();
        assertThat(container.size()).isEqualTo(0);
        assertThat(container.deltaSize()).isEqualTo(0);

        container.add(5);
        container.add("G", 5);
        container.commit();
        assertThat(container.size()).isEqualTo(10);
        assertThat(container.deltaSize()).isEqualTo(0);

        container.discard();
        assertThat(container.size()).isEqualTo(10);
        assertThat(container.deltaSize()).isEqualTo(0);
    }

    @Test
    public void should_pull() {
        {
            ResourceSet container = new ResourceSet();
            container.add(10);
            ResourceSet result = container.pull(null, 11, false);
            assertThat(result.size()).isEqualTo(10);
        }

        {
            ResourceSet container = new ResourceSet();
            container.add(10);
            ResourceSet result = container.pull(null, 11, true);
            assertThat(result.size()).isEqualTo(0);
        }

        {
            ResourceSet container = new ResourceSet();
            container.add(10);
            ResourceSet result = container.pull("G", 11, false);
            assertThat(result.size()).isEqualTo(0);
        }

        {
            ResourceSet container = new ResourceSet();
            container.add(10);
            container.add("G", 10);

            ResourceSet result = container.pull(null, 25, false);
            assertThat(result.size()).isEqualTo(20);
            assertThat(result.get("G")).isEqualTo(10);
        }
    }
}
