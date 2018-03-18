package ru.pk.randgen.gentype;

import ru.pk.randgen.Generator;
import ru.pk.randgen.randomize.Randomize;

/**
 * Created by pk on 11.02.2017.
 */
public class StringGen implements Generator<String> {
    protected final static int DEFAUL_MIN_LENGTH = 1;
    protected final static int DEFAUL_MAX_LENGTH = 10;

    protected static char[] symbolsEng = new char[] {
            'Q','W','E','R','T','Y','U','I','O','P',
            'A','S','D','F','G','H','J','K','L',
            'Z','X','C','V','B','N','M'
    };
    protected static char[] symbolsRus = new char[] {
            'Й','Ц','У','К','Е','Н','Г','Ш','Щ','З',
            'Ф','Ы','В','А','П','Р','О','Л','Д','Ж','Э',
            'Я','Ч','С','М','И','Т','Ь','Б','Ю'
    };

    protected StringGenType genType;
    protected int minLength;
    protected int maxLength;

    public StringGen() {
        this(StringGenType.ENG_SYMBOL, DEFAUL_MIN_LENGTH, DEFAUL_MAX_LENGTH);
    }

    public StringGen(int minLength, int maxLength) {
        this(StringGenType.ENG_SYMBOL, minLength, maxLength);
    }

    public StringGen(StringGenType genType, int minLength, int maxLength) {
        this.genType = genType;
        this.minLength = minLength < 0 ? 0 : minLength;
        this.maxLength = maxLength < 0 ? 0 : maxLength;
    }

    public StringGen(StringGenType genType) {
        this.genType = genType;
        this.minLength = DEFAUL_MIN_LENGTH;
        this.maxLength = DEFAUL_MAX_LENGTH;
    }

    public String gen() {
        String result = "";

        if (StringGenType.RUS_SYMBOL.equals(this.genType)) {
            result = genRusSymbol();
        } else if (StringGenType.ENG_SYMBOL.equals(this.genType)) {
            result = genEngSymbol();
        } else if (StringGenType.RUS_WORD.equals(this.genType)) {
            result = genRusWord();
        } else if (StringGenType.ENG_WORD.equals(this.genType)) {
            result = genEndWord();
        } else {
            double rndSource = Randomize.getInstance().random();
            result += symbolsEng[(int) Math.round(rndSource * (symbolsEng.length - 1))];
        }
        return result;
    }

    private String genRusSymbol() {
        double rndSource = Randomize.getInstance().random();
        return ""+ symbolsRus[(int) Math.round(rndSource * (symbolsRus.length - 1))];
    }

    private String genEngSymbol() {
        double rndSource = Randomize.getInstance().random();
        return ""+ symbolsEng[(int) Math.round(rndSource * (symbolsEng.length - 1))];
    }

    private String genRusWord() {
        double rndSource = Randomize.getInstance().random(); //0-0.9999
        int rndLength = (int) Math.round(rndSource * (maxLength - minLength))+ minLength;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < rndLength; i++) {
            rndSource = Randomize.getInstance().random();
            buffer.append(symbolsRus[(int) Math.round(rndSource * (symbolsRus.length - 1))]);
        }
        return buffer.toString();
    }

    private String genEndWord() {
        double rndSource = Randomize.getInstance().random();
        int rndLength = (int) Math.round(rndSource * (maxLength - minLength));
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < rndLength; i++) {
            rndSource = Randomize.getInstance().random();
            buffer.append(symbolsEng[(int) Math.round(rndSource * (symbolsEng.length - 1))]);
        }
        return buffer.toString();
    }

    public enum StringGenType {
        /**default*/
        ENG_SYMBOL,
        ENG_WORD,
        ENG_NAME,
        RUS_SYMBOL,
        RUS_WORD,
        RUS_FIO
    }
}
