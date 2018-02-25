package ru.pk.objfilltest;

import ru.pk.randgen.annotations.RandGenClass;
import ru.pk.randgen.annotations.RandGenField;

@RandGenClass
public class HomeRoom {

    @RandGenField(type = RandGenField.GeneratorValueType.INTEGER)
    private int windowsQuantity = -1;

    @RandGenField(type = RandGenField.GeneratorValueType.INTEGER)
    private Integer wallsQuantity;

    @RandGenField(type = RandGenField.GeneratorValueType.BOOLEAN)
    private Boolean hasTv;

    @RandGenField(type = RandGenField.GeneratorValueType.LONG)
    private long flowersCount;

    public Boolean isHasTv() {
        return hasTv;
    }

    public void setHasTv(Boolean hasTv) {
        this.hasTv = hasTv;
    }

    public long getWindowsQuantity() {
        return windowsQuantity;
    }

    public Integer getWallsQuantity() {
        return wallsQuantity;
    }

    @Override
    public String toString() {
        return "HomeRoom{" +
                "windowsQuantity=" + windowsQuantity +
                ", wallsQuantity=" + wallsQuantity +
                ", hasTv=" + hasTv +
                ", flowersCount=" + flowersCount +
                '}';
    }
}
