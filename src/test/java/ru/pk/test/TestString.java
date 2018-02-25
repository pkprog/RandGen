package ru.pk.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.StringGen;

public class TestString {
    Logger log = LoggerFactory.getLogger(TestString.class);

    @Test
    public void test1() {
        StringGen b = new StringGen(StringGen.StringGenType.RUS_WORD, 1, 10);
        for (int i = 0; i < 100; i++) {
            log.info(String.valueOf(b.gen()));
        }
    }

}
