package ru.pk.randgen;

import ru.pk.randgen.randomize.Randomize;

/**
 * Created by pk on 11.02.2017.
 */
public class StringGen implements GeneratorOperations<String> {
    private String[] symbolsEng = new String[] {
            "Q","W","E","R","T","Y","U","I","O","P",
            "A","S","D","F","G","H","J","K","L",
            "Z","X","C","V","B","N","M"
    };
    private String[] symbolsRus = new String[] {
            "Й","Ц","У","К","Е","Н","Г","Ш","Щ","З",
            "Ф","Ы","В","А","П","Р","О","Л","Д","Ж","Э",
            "Я","Ч","С","М","И","Т","Ь","Б","Ю"
    };

    public String gen() {
        double source = Randomize.getInstance().random();
        String result = symbolsEng[(int)(source * 100d / (99d / symbolsEng.length))];
        return result;
    }

}
