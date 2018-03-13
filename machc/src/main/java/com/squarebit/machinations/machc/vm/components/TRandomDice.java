package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TObject;

public final class TRandomDice extends TObject {
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
}
