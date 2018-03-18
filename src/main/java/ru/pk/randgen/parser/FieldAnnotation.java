package ru.pk.randgen.parser;

import ru.pk.randgen.annotations.RandGenField;

import java.lang.reflect.Field;

public class FieldAnnotation {
    public Field field;
    public RandGenField annotation;

    public FieldAnnotation(Field field, RandGenField annotation) {
        this.field = field;
        this.annotation = annotation;
    }
}
