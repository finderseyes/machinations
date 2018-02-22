package com.squarebit.machinations.engine;

import org.assertj.core.data.Offset;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ArithmeticExpressionTests {
    @Test
    public void should_evalute_arithmetic_expressions() {
        assertThat(Addition.of(IntNumber.of(2), IntNumber.of(3)).eval()).isEqualTo(5);
        assertThat(Addition.of(IntNumber.of(9), Negation.of(IntNumber.of(3))).eval()).isEqualTo(6);
        assertThat(Subtraction.of(IntNumber.of(2), IntNumber.of(3)).eval()).isEqualTo(-1);
        assertThat(Subtraction.of(IntNumber.of(2), Negation.of(IntNumber.of(3))).eval()).isEqualTo(5);

        assertThat(
                Multiplication.of(IntNumber.of(2), IntNumber.of(3)).eval()
        ).isEqualTo(6);

        assertThat(
                Multiplication.of(
                        IntNumber.of(4),
                        Addition.of(IntNumber.of(2), IntNumber.of(3))
                ).eval()
        ).isEqualTo(20);

        assertThat(
                Multiplication.of(
                        IntNumber.of(4),
                        Subtraction.of(IntNumber.of(2), IntNumber.of(3))
                ).eval()
        ).isEqualTo(-4);
    }

    @Test
    public void should_support_variable() {
        AtomicInteger x = new AtomicInteger();
        Variable xvar = Variable.of("x", x::get);

        x.set(10);
        assertThat(Addition.of(IntNumber.of(2), xvar).eval()).isEqualTo(12);

        x.set(20);
        assertThat(Addition.of(IntNumber.of(2), xvar).eval()).isEqualTo(22);
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
                () -> ProbableNumber.of(0.5f).eval() == 1
        )).isCloseTo(0.5f, Offset.offset(3e-2f));

        assertThat(predicateProbability(
                () -> Addition.of(IntNumber.of(1), ProbableNumber.of(0.0f)).eval() == 2
        )).isCloseTo(0.0f, Offset.offset(3e-2f));

        assertThat(predicateProbability(
                () -> Addition.of(IntNumber.of(1), ProbableNumber.of(1.0f)).eval() == 2
        )).isCloseTo(1.0f, Offset.offset(3e-2f));

        assertThat(predicateProbability(
                () -> Addition.of(IntNumber.of(1), ProbableNumber.of(0.75f)).eval() == 2
        )).isCloseTo(0.75f, Offset.offset(3e-2f));
    }
}
