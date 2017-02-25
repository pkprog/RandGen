package ru.pk.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.IntegerGen;

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
        geneneralTest(1, 15);
    }

    @Test
    public void testBorders2() {
        geneneralTest(-1, -1);
    }

    @Test
    public void testBorders3() {
        geneneralTest(-10, -8);
    }

    private void geneneralTest(int start, int end) {
        log.info("start=" + start + ", end=" + end);

        boolean startCrossed = false, endCrossed = false;

        IntegerGen b = new IntegerGen(start, end);
        boolean foundStart = false, foundEnd = false;
        for (int i = 0; i < Math.abs(end - start + 1) * 10; i++) {
            Integer obj = b.gen();
            if (obj == start) foundStart = true;
            if (obj == end) foundEnd = true;
            if (obj < start) startCrossed = true;
            if (obj > end) endCrossed = true;

            log.info(String.valueOf(obj));
        }

        Assert.assertTrue("Start not found", foundStart);
        Assert.assertTrue("End not found", foundEnd);
        Assert.assertFalse("Found < start", startCrossed);
        Assert.assertFalse("Found > end", endCrossed);
    }

}
