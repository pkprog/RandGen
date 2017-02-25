package ru.pk.randgen;

import ru.pk.randgen.randomize.Randomize;

/**
 * Created by pk on 11.02.2017.
 */
public class CharGen implements GeneratorOperations<Character> {
    public enum CharGenType {
        /**default*/
        ENG,
        RUS
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

    protected CharGenType genType = CharGenType.ENG;

    public CharGen() {}

    public CharGen(CharGenType genType) {
        if (genType != null) {
            this.genType = genType;
        }
    }

    public Character gen() {
        double source = Randomize.getInstance().random();
        Character result;

        if (CharGenType.RUS.equals(this.genType)) {
            result = symbolsRus[(int) Math.round(source * (symbolsRus.length - 1))];
        } else {
            result = symbolsEng[(int) Math.round(source * (symbolsEng.length - 1))];
        }

        return result;
    }

}
