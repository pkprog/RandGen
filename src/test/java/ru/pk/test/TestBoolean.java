package ru.pk.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.BooleanGen;

public class TestBoolean {
    Logger log = LoggerFactory.getLogger(TestBoolean.class);

    @Test
    public void test() {
        BooleanGen b = new BooleanGen();
        boolean foundTrue = false, foundFalse = false;
        for (int i = 0; i < 1000; i++) {
            Boolean obj = b.gen();
            if (obj) foundTrue = true;
            if (!obj) foundFalse = true;
//            log.info(String.valueOf(obj));
        }

        Assert.assertTrue(foundTrue);
        Assert.assertTrue(foundFalse);
    }

}
