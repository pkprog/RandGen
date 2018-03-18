package ru.pk.randgen.gentype;

import ru.pk.randgen.Generator;
import ru.pk.randgen.randomize.Randomize;

/**
 * Created by pk on 11.02.2017.
 */
public class DoubleGen implements Generator<Double> {

    public Double gen() {
        double source = Randomize.getInstance().random();
        return source;
    }

}
