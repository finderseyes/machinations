package com.squarebit.machinations.parsers;

import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

@Data
public class DiceTerm extends AbstractDiceTerm {
    private int times = 1;
    private int faces = 6;

    public DiceTerm() {

    }

    public int getTimes() {
        return times;
    }

    public DiceTerm setTimes(int times) {
        this.times = times;
        return this;
    }

    public int getFaces() {
        return faces;
    }

    public DiceTerm setFaces(int faces) {
        this.faces = faces;
        return this;
    }

    @Override
    public int evaluate() {
        int sum = 0;
        for (int i = 0; i < this.times; i++)
            sum += RandomUtils.nextInt(1, this.faces + 1);
        return sum * this.getSign();
    }
}
