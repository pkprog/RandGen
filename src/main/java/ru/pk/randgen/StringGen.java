package ru.pk.randgen;

import ru.pk.randgen.randomize.Randomize;

/**
 * Created by pk on 11.02.2017.
 */
public class StringGen implements GeneratorOperations<String> {
    public enum StringGenType {
        /**default*/
        ENG,
        RUS,
        RUS_WORDS
    }
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

    protected StringGenType genType = StringGenType.ENG;

    public StringGen() {
    }

    public StringGen(StringGenType genType) {
        if (genType != null) {
            this.genType = genType;
        }
    }

    public String gen() {
        double source = Randomize.getInstance().random();
        String result = "";

        if (StringGenType.RUS.equals(this.genType)) {
            result += symbolsRus[(int) Math.round(source * (symbolsRus.length - 1))];
        } else if (StringGenType.RUS_WORDS.equals(this.genType)) {

        } else {
            result += symbolsEng[(int) Math.round(source * (symbolsEng.length - 1))];
        }
        return result;
    }

}
