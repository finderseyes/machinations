package com.squarebit.machinations.machc.ast.expressions;

/**
 * The random draw literal.
 */
public final class GRandomDice implements GExpression {
    private static final String DRAW_PREFIX = "draw";
    private static final int DRAW_PREFIX_LENGTH = DRAW_PREFIX.length();

    private final int times;
    private final int faces;

    /**
     * Instantiates a new G random dice.
     *
     * @param times the times
     * @param faces the faces
     */
    public GRandomDice(int times, int faces) {
        this.times = times;
        this.faces = faces;
    }

    /**
     * Gets times.
     *
     * @return the times
     */
    public int getTimes() {
        return times;
    }

    /**
     * Gets faces.
     *
     * @return the faces
     */
    public int getFaces() {
        return faces;
    }

    /**
     * Parse g random dice.
     *
     * @param text the text
     * @return the g random dice
     */
    public static GRandomDice parse(String text) {
        if (text.startsWith(DRAW_PREFIX)) {
            int faces = Integer.parseInt(text.substring(DRAW_PREFIX_LENGTH));
            return new GRandomDice(1, faces);
        }
        else {
            if (text.equals("D"))
                return new GRandomDice(1, 6);
            else if (text.charAt(0) == 'D')
                return new GRandomDice(1, Integer.parseInt(text.substring(1)));
            else if (text.charAt(text.length() - 1) == 'D')
                return new GRandomDice(Integer.parseInt(text.substring(0, text.length() - 1)), 6);
            else {
                String[] parts = text.split("D");
                return new GRandomDice(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }
        }
    }
}
