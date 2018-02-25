package ru.pk.randgen.randomize;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pk on 11.02.2017.
 * Creates and handles randomize engines, implements MakeRandomSequence
 * By default creates MathSequence
 */
public class Randomize {
    private volatile static Map<MakeRandomSequence<Double>, Randomize> randomizers = new HashMap<>();
    private MakeRandomSequence<Double> randSequence;

    private Randomize(MakeRandomSequence randSequence) {
        this.randSequence = randSequence;
    }

    private static Randomize addToMap(MakeRandomSequence seq) {
        Randomize r = null;
        synchronized (Randomize.class) {
            if (randomizers.isEmpty()) {
                r = new Randomize(seq);
                randomizers.put(seq, new Randomize(seq));
            } else {
                if (randomizers.containsKey(seq)) {
                    r = randomizers.get(seq);
                } else {
                    randomizers.put(seq, new Randomize(seq));
                }
            }
        }
        return r;
    }

    public static Randomize getInstance() {
        return getInstance(getDefaultSequence());
    }

    public static Randomize getInstance(MakeRandomSequence userRandSequence) {
        if (userRandSequence == null) {
            throw new NullPointerException("Please set MakeRandomSequence");
        }
        Randomize r = null;
        if (randomizers.isEmpty()) {
            r = addToMap(userRandSequence);
        } else {
            if (randomizers.containsKey(userRandSequence)) {
                r = randomizers.get(userRandSequence);
            } else {
                r = addToMap(userRandSequence);
            }
        }
        return r;
    }

    public double random() {
        return randSequence.random();
    }

    protected static MakeRandomSequence getDefaultSequence() {
        return MathSequence.getInstance();
    }

}
