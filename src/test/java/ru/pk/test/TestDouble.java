package ru.pk.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.BooleanGen;
import ru.pk.randgen.DoubleGen;

public class TestDouble {
    Logger log = LoggerFactory.getLogger(TestDouble.class);

    @Test
    public void test1() {
        DoubleGen b = new DoubleGen();
        for (int i = 0; i < 100; i++) {
            log.info(String.valueOf(b.gen()));
        }
    }

}
