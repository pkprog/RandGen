package ru.pk.objfilltest;

import ru.pk.randgen.annotations.RandGenField;

public class AbstractRoom {

    @RandGenField(type = RandGenField.GeneratorValueType.INTEGER)
    private int wallsColor;

    public int getWallsColor() {
        return wallsColor;
    }

}
