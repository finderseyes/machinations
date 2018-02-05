package com.squarebit.machinations.models;

import org.junit.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class IntSupplierFactoryTests {
    @Test
    public void should_support_constant_expression() throws Exception {
        IntSupplierFactory factory = new IntSupplierFactory();

        {
            Supplier<Integer> supplier = factory.fromExpression("D+3");
            supplier = factory.fromExpression("D");
            supplier = factory.fromExpression("1D");
            supplier = factory.fromExpression("1D5");
            supplier = factory.fromExpression("1D5+10");
            supplier = factory.fromExpression("1D5-10");
            assertThat(supplier).isNotNull();
            assertThat(supplier.get()).isEqualTo(3);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression(" 10 ");
            assertThat(supplier).isNotNull();
            assertThat(supplier.get()).isEqualTo(10);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression("-3");
            assertThat(supplier).isNotNull();
            assertThat(supplier.get()).isEqualTo(-3);
        }

        {
            Supplier<Integer> supplier = factory.fromExpression(" -10 ");
            assertThat(supplier).isNotNull();
            assertThat(supplier.get()).isEqualTo(-10);
        }
    }
}
