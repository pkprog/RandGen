package ru.pk.randgen;

/**
 * Created by pk on 11.02.2017.
 */
public class BooleanGen implements GeneratorOperations<Boolean> {

    public Boolean gen() {
        double source = Math.random() * 10;
        return ((int) source % 2) == 0;
    }

}
