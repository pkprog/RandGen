package ru.pk.randgen.parser;

import ru.pk.randgen.annotations.RandGenClass;
import ru.pk.randgen.annotations.RandGenField;

public interface Parser {
    Class targetClassAnnotation = RandGenClass.class;
    Class targetFieldAnnotation = RandGenField.class;
    String targetClassAnnotationName = targetClassAnnotation.getName();
    String targetFieldAnnotationName = targetFieldAnnotation.getName();

    ParserResult parse(Class c);
}
