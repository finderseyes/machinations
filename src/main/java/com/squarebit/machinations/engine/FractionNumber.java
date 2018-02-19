package com.squarebit.machinations.engine;

public class FractionNumber extends ArithmeticExpression {
    public int numerator = 1;
    public int denominator = 1;

    public int getNumerator() {
        return numerator;
    }

    public FractionNumber setNumerator(int numerator) {
        this.numerator = numerator;
        return this;
    }

    public int getDenominator() {
        return denominator;
    }

    public FractionNumber setDenominator(int denominator) {
        this.denominator = denominator;
        return this;
    }

    /**
     * Evaluates the expression and returns its result.
     *
     * @return integer result.
     */
    @Override
    public int evaluate() {
        return 0;
    }

    /**
     * Evaluate as probable and return probability.
     *
     * @return probability
     */
    @Override
    public float evaluateAsProbable() {
        return numerator / (float)denominator;
    }

    public static FractionNumber of(int numerator, int denominator) {
        return new FractionNumber().setNumerator(numerator).setDenominator(denominator);
    }
}
