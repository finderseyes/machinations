package com.squarebit.machinations.engine;

import org.apache.commons.lang.math.IntRange;
import org.assertj.core.data.Offset;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ArithmeticExpressionTests {
    @Test
    public void should_evalute_arithmetic_expressions() {
        assertThat(Addition.of(Number.of(2), Number.of(3)).evaluate()).isEqualTo(5);
        assertThat(Addition.of(Number.of(9), Negation.of(Number.of(3))).evaluate()).isEqualTo(6);
        assertThat(Subtraction.of(Number.of(2), Number.of(3)).evaluate()).isEqualTo(-1);
        assertThat(Subtraction.of(Number.of(2), Negation.of(Number.of(3))).evaluate()).isEqualTo(5);

        assertThat(
                Multiplication.of(Number.of(2), Number.of(3)).evaluate()
        ).isEqualTo(6);

        assertThat(
                Multiplication.of(
                        Number.of(4),
                        Addition.of(Number.of(2), Number.of(3))
                ).evaluate()
        ).isEqualTo(20);

        assertThat(
                Multiplication.of(
                        Number.of(4),
                        Subtraction.of(Number.of(2), Number.of(3))
                ).evaluate()
        ).isEqualTo(-4);
    }

    @Test
    public void should_support_variable() {
        AtomicInteger x = new AtomicInteger();
        Variable xvar = Variable.of("x", x::get);

        x.set(10);
        assertThat(Addition.of(Number.of(2), xvar).evaluate()).isEqualTo(12);

        x.set(20);
        assertThat(Addition.of(Number.of(2), xvar).evaluate()).isEqualTo(22);
    }

    private float predicateProbability(Supplier<Boolean> predicate) {
        int times = 10000;
        int count = IntStream.range(0, times).map(i -> {
            if (predicate.get())
                return 1;
            else
                return 0;
        }).sum();

        return (count / (float)times);
    }

    @Test
    public void should_support_probable_number() {
        assertThat(predicateProbability(
                () -> ProbableNumber.of(0.5f).evaluate() == 1
        )).isCloseTo(0.5f, Offset.offset(3e-2f));

        assertThat(predicateProbability(
                () -> Addition.of(Number.of(1), ProbableNumber.of(0.0f)).evaluate() == 2
        )).isCloseTo(0.0f, Offset.offset(3e-2f));

        assertThat(predicateProbability(
                () -> Addition.of(Number.of(1), ProbableNumber.of(1.0f)).evaluate() == 2
        )).isCloseTo(1.0f, Offset.offset(3e-2f));

        assertThat(predicateProbability(
                () -> Addition.of(Number.of(1), ProbableNumber.of(0.75f)).evaluate() == 2
        )).isCloseTo(0.75f, Offset.offset(3e-2f));
    }
}
