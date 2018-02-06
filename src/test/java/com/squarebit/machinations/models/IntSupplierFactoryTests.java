package com.squarebit.machinations.models;

import org.assertj.core.data.Offset;
import org.junit.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class IntSupplierFactoryTests {
    private void assertUniform(Supplier<Integer> supplier, int from, int to) {
        int samples = 1000;
        int range = (to - from) + 1;

        int[] count = new int[range];
        float[] bins = new float[range];
        float kullbackLeiblerDistance = 0.0f;

        for (int i = 0; i < samples; i++) {
            int value = supplier.get();
            count[value - from] += 1;
        }

        float qbin = 1.0f / range;
        for (int i = 0; i < range; i++) {
            bins[i] = (float) count[i] / samples;
            kullbackLeiblerDistance += bins[i] * Math.log(bins[i] / qbin);
        }

        assertThat(kullbackLeiblerDistance).isCloseTo(0.0f, Offset.offset(1e-2f));
    }

    private void assertInRange(Supplier<Integer> supplier, int from, int to) {
        int samples = 1000;
        int range = (to - from) + 1;

        for (int i = 0; i < samples; i++) {
            int value = supplier.get();
            assertThat(value).isBetween(from, to);
        }
    }

    @Test
    public void should_support_constant_expression() throws Exception {
        IntSupplierFactory factory = new IntSupplierFactory();

        {
            Supplier<Integer> supplier = factory.fromExpression("3");
            assertThat(supplier.get()).isEqualTo(3);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression(" 10 ");
            assertThat(supplier.get()).isEqualTo(10);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("-3");
            assertThat(supplier.get()).isEqualTo(-3);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression(" -10 ");
            assertThat(supplier.get()).isEqualTo(-10);
        }
    }

    @Test
    public void should_support_dice_expression() throws Exception {
        IntSupplierFactory factory = new IntSupplierFactory();

        {
            Supplier<Integer> supplier = factory.fromExpression("D");
            assertUniform(supplier, 1, 6);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("D10");
            assertUniform(supplier, 1, 10);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("1D");
            assertUniform(supplier, 1, 6);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("1D10");
            assertUniform(supplier, 1, 10);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("2D");
            assertInRange(supplier, 2, 12);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("2D10");
            assertInRange(supplier, 2, 20);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("D+3");
            assertUniform(supplier, 4, 9);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("D10+5");
            assertUniform(supplier, 6, 15);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("1D+4");
            assertUniform(supplier, 5, 10);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("1D10+5");
            assertUniform(supplier, 6, 15);
        }
    }

    @Test
    public void should_support_multiple_dice_expressions() throws Exception {
        IntSupplierFactory factory = new IntSupplierFactory();

        {
            Supplier<Integer> supplier = factory.fromExpression("D+D");
            assertInRange(supplier, 2, 12);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("D+D5-5");
            assertInRange(supplier, -3, 6);
        }
    }
}
