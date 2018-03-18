package ru.pk.randgen;

import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.gentype.BooleanGen;
import ru.pk.randgen.gentype.DoubleGen;
import ru.pk.randgen.gentype.IntegerGen;
import ru.pk.randgen.gentype.StringGen;

public abstract class ValueFactory<RETURN_TYPE> {
    protected RandGenField ann;

    public ValueFactory(RandGenField ann) {
        this.ann = ann;
    }

    public abstract RETURN_TYPE makeValue();

    private static volatile BooleanGen booleanGenInstance = null;
    private static volatile IntegerGen integerGenInstance = null;
    private static volatile StringGen stringGenInstance = null;
    private static volatile DoubleGen doubleGenInstance = null;

    private static final Object syncBooleanGenObj = new Object();

    public static BooleanGen getBooleanGen() {
        if (booleanGenInstance != null) {
            return booleanGenInstance;
        } else {
            synchronized(syncBooleanGenObj) {
                if (booleanGenInstance != null)
                    return booleanGenInstance;
                else {
                    BooleanGen gen = new BooleanGen();
                    booleanGenInstance = gen;
                    return booleanGenInstance;
                }
            }
        }
    }

    private static final Object syncIntegerGenObj = new Object();

    public static IntegerGen getIntegerGen() {
        if (integerGenInstance != null) {
            return integerGenInstance;
        } else {
            synchronized(syncIntegerGenObj) {
                if (integerGenInstance != null)
                    return integerGenInstance;
                else {
                    IntegerGen gen = new IntegerGen();
                    integerGenInstance = gen;
                    return integerGenInstance;
                }
            }
        }
    }

    private static final Object syncStringGenObj = new Object();

    public static StringGen getStringGen(RandGenField ann) {
        if (stringGenInstance != null) {
            return stringGenInstance;
        } else {
            synchronized(syncStringGenObj) {
                if (stringGenInstance != null)
                    return stringGenInstance;
                else {
                    int minLen = ann.stringMinLength();
                    int maxLen = ann.stringMaxLength();
                    StringGen gen = new StringGen(StringGen.StringGenType.RUS_WORD, minLen, maxLen);
                    stringGenInstance = gen;
                    return stringGenInstance;
                }
            }
        }
    }

    private static final Object syncDoubleGenObj = new Object();

    public static DoubleGen getDoubleGen() {
        if (doubleGenInstance != null) {
            return doubleGenInstance;
        } else {
            synchronized(syncDoubleGenObj) {
                if (doubleGenInstance != null)
                    return doubleGenInstance;
                else {
                    DoubleGen gen = new DoubleGen();
                    doubleGenInstance = gen;
                    return doubleGenInstance;
                }
            }
        }
    }


}
