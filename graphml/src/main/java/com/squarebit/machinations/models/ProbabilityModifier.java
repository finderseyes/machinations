package com.squarebit.machinations.models;

import com.squarebit.machinations.engine.Percentage;

/**
 * The probability modifier.
 */
public class ProbabilityModifier extends Modifier {
    private Percentage value;
    private int sign = 1;

    /**
     * Gets value.
     *
     * @return the value
     */
    public Percentage getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     * @return the value
     */
    public ProbabilityModifier setValue(Percentage value) {
        this.value = value;
        return this;
    }

    /**
     * Gets sign.
     *
     * @return the sign
     */
    public int getSign() {
        return sign;
    }

    /**
     * Sets sign.
     *
     * @param sign the sign
     * @return the sign
     */
    public ProbabilityModifier setSign(int sign) {
        sign = sign < 0 ? -1 : 1;
        this.sign = sign;
        return this;
    }
}
