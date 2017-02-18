package ru.pk.randgen;

import ru.pk.randgen.randomize.Randomize;

/**
 * Created by pk on 11.02.2017.
 */
public class DoubleGen implements GeneratorOperations<Double> {

    public Double gen() {
        double source = Randomize.getInstance().random();
        return source;
    }

}
