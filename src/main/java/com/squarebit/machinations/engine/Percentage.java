package com.squarebit.machinations.engine;

public class Percentage {
    private int value;

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
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
        percentage.value = Integer.parseInt(text.substring(0, text.length() - 1));
        return percentage;
    }
}
