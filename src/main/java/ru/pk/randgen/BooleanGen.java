package ru.pk.randgen;

import ru.pk.randgen.randomize.Randomize;

/**
 * Created by pk on 11.02.2017.
 */
public class BooleanGen implements GeneratorOperations<Boolean> {
    private Randomize randomize;

    public BooleanGen() {
        this.randomize = Randomize.getInstance();
    }

    public Boolean gen() {
        double source = this.randomize.random() * 10;
        return ((int) source % 2) == 0;
    }

}
