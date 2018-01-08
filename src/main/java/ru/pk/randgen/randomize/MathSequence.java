package ru.pk.randgen.randomize;

/**
 * Created by pk on 11.02.2017.
 * Generate random sequence using Math.random()
 */
public class MathSequence implements MakeRandomSequence {
    private static MathSequence instance;

    @Override
    public double random() {
        return Math.random();
    }

    private MathSequence() {}

    public static MakeRandomSequence getInstance() {
        MathSequence r = instance;
        if (instance == null) {
            synchronized(MathSequence.class) {
                if (instance == null) {
                    r = instance = new MathSequence();
                }
            }
        }
        return r;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof MathSequence))return false;
        return true;
    }
}
