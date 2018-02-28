package com.squarebit.machinations.engine;

public class Percentage {
    private IntegerExpression value = FixedInteger.of(100);

    /**
     * Gets value.
     *
     * @return the value
     */
    public int eval() {
        return value.eval();
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public IntegerExpression getValue() {
        return value;
    }

    /**
     * Of percentage.
     *
     * @param value the value
     * @return the percentage
     */
    public static Percentage of(int value) {
        Percentage percentage = new Percentage();
        percentage.value = FixedInteger.of(value);
        return percentage;
    }

    /**
     *
     * @param value
     * @return
     */
    public static Percentage of(IntegerExpression value) {
        Percentage percentage = new Percentage();
        percentage.value = value;
        return percentage;
    }

    /**
     * Parse percentage.
     *
     * @param text the text
     * @return the percentage
     */
    public static Percentage parse(String text) {
        Percentage percentage = new Percentage();
        percentage.value = FixedInteger.of(Integer.parseInt(text.substring(0, text.length() - 1)));
        return percentage;
    }
}
