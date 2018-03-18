package ru.pk.randgen.parser;

import java.util.LinkedList;
import java.util.List;

public class ParserResult {
    public Class parsedClass;
    public List<FieldAnnotation> goodFields = new LinkedList<>();
    public List<FieldAnnotation> badFields = new LinkedList<>();

    public ParserResult(Class parsedClass, List<FieldAnnotation> goodFields, List<FieldAnnotation> badFields) {
        this.parsedClass = parsedClass;
        this.goodFields = goodFields;
        this.badFields = badFields;
    }
}
