package ru.pk.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.gentype.CharGen;

public class TestChar {
    Logger log = LoggerFactory.getLogger(TestChar.class);

    @Test
    public void test1() {
        CharGen b = new CharGen();
        for (int i = 0; i < 100; i++) {
            log.info(String.valueOf(b.gen()));
        }
    }

}
