package ru.pk.randgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.annotations.RandGenClass;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.exceptions.GenRuntimeException;
import ru.pk.randgen.gentype.BooleanGen;
import ru.pk.randgen.gentype.DoubleGen;
import ru.pk.randgen.gentype.IntegerGen;
import ru.pk.randgen.gentype.StringGen;
import ru.pk.randgen.parser.FieldAnnotation;
import ru.pk.randgen.parser.ParseByFields;
import ru.pk.randgen.parser.ParserResult;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class AnnotationsDataFill {
    private final static Logger LOG = LoggerFactory.getLogger(AnnotationsDataFill.class);

    private static Class targetClassAnnotation = RandGenClass.class;
    private static String targetClassAnnotationName = targetClassAnnotation.getName();
    private static Class targetFieldAnnotation = RandGenField.class;
    private static String targetFieldAnnotationName = targetFieldAnnotation.getName();

    private DecimalFormat decimalFormat = new DecimalFormat("0.000");

    private ScanType scanType;

    public AnnotationsDataFill() {
        this(ScanType.BY_FIELDS);
    }

    public AnnotationsDataFill(ScanType scanType) {
        LOG.debug("Create data filler type={}", scanType);
        this.scanType = scanType;
    }

    //Поиск пометок аннотациями и заполнение данными
    public void fill(Object obj) {
        final Class c = obj.getClass();
        RandGenClass foundClassAnn = (RandGenClass) c.getAnnotation(targetClassAnnotation);
        if (foundClassAnn == null) {
            LOG.debug("Not found class-annotation {} in class {}", targetClassAnnotationName, obj.getClass().getName());
            return;
        }
        LOG.debug("Found class {} with annotation {}", obj.getClass().getName(), targetClassAnnotationName);

        ParserResult parserResult = collectFields(c);

        if (scanType.equals(ScanType.BY_CLASS)) {
            //По классу. Не глядя на аннотирование полей
            fillByClass();
        } else if (scanType.equals(ScanType.BY_FIELDS)) {
            //По аннотированным полям. Класс аннотировать тоже нужно.
            fillByFields(obj, parserResult.goodFields);
        }
    }

    private void fillByClass() {
        throw new GenRuntimeException("Method not implemented");
    }

    private void fillByFields(Object obj, List<FieldAnnotation> goodFields) {
        Filler filler = new Filler();
        filler.fill(obj, goodFields);
    }

    private ParserResult collectFields(Class c) {
        ParseByFields parser = new ParseByFields();
        ParserResult result = parser.parse(c);
        return result;
    }

    public enum ScanType {
        BY_CLASS,
        BY_FIELDS
    }

}
