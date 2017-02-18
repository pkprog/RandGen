package ru.pk.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.IntegerGen;

import java.util.Set;
import java.util.TreeSet;

public class TestInteger {
    Logger log = LoggerFactory.getLogger(TestInteger.class);

    @Test
    public void test10() {
        IntegerGen b = new IntegerGen();
        for (int i = 0; i < 100; i++) {
            log.info(String.valueOf(b.gen()));
        }
    }

    @Test
    public void testBorders() {
        final int start = 1;
        final int end = 15;
        IntegerGen b = new IntegerGen(start, end);
        boolean foundStart = false, foundEnd = false;
        for (int i = 0; i < 1000; i++) {
            Integer obj = b.gen();
            if (obj == start) foundStart = true;
            if (obj == end) foundEnd = true;
//            log.info(String.valueOf(obj));
        }

        Assert.assertTrue(foundStart);
        Assert.assertTrue(foundEnd);
    }

    @Test
    public void testBorders2() {
        final int start = -1;
        final int end = 15;
        IntegerGen b = new IntegerGen(start, end);
        boolean foundStart = false, foundEnd = false;
        for (int i = 0; i < 1000; i++) {
            Integer obj = b.gen();
            if (obj == start) foundStart = true;
            if (obj == end) foundEnd = true;
            log.info(String.valueOf(obj));
        }

        Assert.assertTrue(foundStart);
        Assert.assertTrue(foundEnd);
    }

}
