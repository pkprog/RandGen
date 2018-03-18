package ru.pk.randgen.gentype;

import ru.pk.randgen.Generator;
import ru.pk.randgen.randomize.Randomize;

/**
 * Created by pk on 11.02.2017.
 * Thanks to Ann
 */
public class BooleanGen implements Generator<Boolean> {
    private Randomize randomize;

    public BooleanGen() {
        this.randomize = Randomize.getInstance();
    }

    public Boolean gen() {
        double source = this.randomize.random() * 10;
        return ((int) source % 2) == 0;
    }

}
