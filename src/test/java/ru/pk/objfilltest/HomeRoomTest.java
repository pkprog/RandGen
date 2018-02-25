package ru.pk.objfilltest;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.AnnotationsDataFill;

public class HomeRoomTest {
    private final static Logger LOG = LoggerFactory.getLogger(HomeRoomTest.class);

    @Test
    public void test() {
        HomeRoom homeRoom = new HomeRoom();
        AnnotationsDataFill annotationsDataFill = new AnnotationsDataFill(AnnotationsDataFill.ScanType.BY_FIELDS);
        annotationsDataFill.fill(homeRoom);

        Assert.assertTrue(homeRoom.isHasTv() != null);
        Assert.assertTrue(homeRoom.getWindowsQuantity() >= 0);
        Assert.assertTrue(homeRoom.getWallsQuantity() != null);

        LOG.warn("Tests passed");

        for (int i = 0; i < 100; i++) {
            HomeRoom h = new HomeRoom();
            String h1 = h.toString();
            annotationsDataFill.fill(h);
            String h2 = h.toString();
            LOG.info("{}: {} --> {}", i, h1, h2);
        }
    }

}
