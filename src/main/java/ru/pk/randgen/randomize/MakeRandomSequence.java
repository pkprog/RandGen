package ru.pk.randgen.randomize;

/**
 * Created by pk on 11.02.2017.
 */
public interface MakeRandomSequence<RESULT_TYPE extends Number> {
    RESULT_TYPE random();
}
