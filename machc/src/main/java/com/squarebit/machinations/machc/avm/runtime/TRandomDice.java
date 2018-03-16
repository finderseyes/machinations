package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;
import org.apache.commons.lang3.RandomUtils;

public class TRandomDice implements TObject {
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
     * Generate a new integer value.
     *
     * @return the integer
     */
    public TInteger generate() {
        int sum = 0;
        for (int i = 0; i < this.times; i++)
            sum += RandomUtils.nextInt(1, this.faces + 1);
        return new TInteger(sum);
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.RANDOM_DICE_TYPE;
    }
}
