package ru.pk.randgen.gentype;

import ru.pk.randgen.Generator;
import ru.pk.randgen.exceptions.GenRuntimeException;
import ru.pk.randgen.randomize.Randomize;

/**
 * Created by pk on 11.02.2017.
 */
public class IntegerGen implements Generator<Integer> {
    private Randomize randomize;
    private boolean useMinMax = false;
    private int min = 0;
    private int max = 0;

    /**
     * By default generates integers: [1, 10]
     */
    public IntegerGen() {
        this.randomize = Randomize.getInstance();
    }

    public IntegerGen(int min, int max) {
        this();
        if (min > max) {
            throw new GenRuntimeException("min > max");
        }
        if (max - min > Integer.MAX_VALUE) {
            throw new GenRuntimeException("Interval [min, max] > Integer.MAX_VALUE");
        }

        this.useMinMax = true;
        this.min = min;
        this.max = max;
    }

    public Integer gen() {
        Integer result;
        double source = this.randomize.random();
        if (useMinMax) {
            result = (int) Math.round(source * (this.max - this.min) + this.min);
        } else {
            result = (int) (source * 10);
        }

        return result;
    }
}
