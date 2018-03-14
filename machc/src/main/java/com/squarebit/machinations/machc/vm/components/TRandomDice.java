package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;
import org.apache.commons.lang3.RandomUtils;

public final class TRandomDice extends TIntegerGenerator {
    private int times;
    private int faces;

    /**
     * Instantiates a new T random dice.
     *
     * @param times the times
     * @param faces the faces
     */
    public TRandomDice(int times, int faces) {
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
     * Gets the type of this object.
     *
     * @return object type.
     */
    @Override
    public TType getType() {
        return Types.RANDOM_DICE_TYPE;
    }

    /**
     * Generate a new integer value.
     *
     * @return the integer
     */
    @Override
    public TInteger generate() {
        int sum = 0;
        for (int i = 0; i < this.times; i++)
            sum += RandomUtils.nextInt(1, this.faces + 1);
        return new TInteger(sum);
    }
}
