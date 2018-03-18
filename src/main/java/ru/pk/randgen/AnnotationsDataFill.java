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
    private static String targetClassAnnotationName;
    private static Class targetFieldAnnotation = RandGenField.class;
    private static String targetFieldAnnotationName;

    private DecimalFormat decimalFormat = new DecimalFormat("0.000");

    static {
        targetClassAnnotationName = targetClassAnnotation.getName();
        targetFieldAnnotationName = targetFieldAnnotation.getName();
    }

    //init
    private static volatile BooleanGen booleanGenInstance = null;
    private static volatile IntegerGen integerGenInstance = null;
    private static volatile StringGen stringGenInstance = null;
    private static volatile DoubleGen doubleGenInstance = null;

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

    private Boolean generateBoolean(RandGenField ann) {
        if (RandGenField.GeneratorValueType.BOOLEAN.equals(ann.type()))
            return getBooleanGen().gen();
        if (RandGenField.GeneratorValueType.STRING.equals(ann.type())) {
            StringGen sg = getStringGen(ann);
            String tempS = sg.gen();
            return !tempS.equals("");
        }
        if (RandGenField.GeneratorValueType.INTEGER.equals(ann.type())) {
            return getIntegerGen().gen() != 0;
        }
        if (RandGenField.GeneratorValueType.LONG.equals(ann.type())) {
            return getIntegerGen().gen() != 0;
        }
        if (RandGenField.GeneratorValueType.DOUBLE.equals(ann.type())) {
            return getDoubleGen().gen() != 0d;
        }
        throw new GenRuntimeException("Not found generator with Boolean-result for annotation type "+ ann.type());
    }

    private Integer generateInteger(RandGenField ann) {
        if (RandGenField.GeneratorValueType.BOOLEAN.equals(ann.type()))
            return getBooleanGen().gen() ? 1 : 0;
        if (RandGenField.GeneratorValueType.INTEGER.equals(ann.type())) {
            return getIntegerGen().gen();
        }
        if (RandGenField.GeneratorValueType.LONG.equals(ann.type())) {
            return getIntegerGen().gen();
        }
        throw new GenRuntimeException("Not found generator with Integer-result for annotation type "+ ann.type());
    }

    private Long generateLong(RandGenField ann) {
        if (RandGenField.GeneratorValueType.BOOLEAN.equals(ann.type()))
            return getBooleanGen().gen() ? 1L : 0L;
        if (RandGenField.GeneratorValueType.INTEGER.equals(ann.type())) {
            return getIntegerGen().gen().longValue();
        }
        if (RandGenField.GeneratorValueType.LONG.equals(ann.type())) {
            return getIntegerGen().gen().longValue();
        }
        if (RandGenField.GeneratorValueType.DOUBLE.equals(ann.type())) {
            return Math.round(getDoubleGen().gen());
        }
        throw new GenRuntimeException("Not found generator with Long-result for annotation type "+ ann.type());
    }

    private String generateString(RandGenField ann) {
        if (RandGenField.GeneratorValueType.BOOLEAN.equals(ann.type())) {
            return String.valueOf(getBooleanGen().gen());
        }
        if (RandGenField.GeneratorValueType.STRING.equals(ann.type())) {
            return getStringGen(ann).gen();
        }
        if (RandGenField.GeneratorValueType.INTEGER.equals(ann.type())) {
            return String.valueOf(getIntegerGen().gen());
        }
        if (RandGenField.GeneratorValueType.LONG.equals(ann.type())) {
            return String.valueOf(getIntegerGen().gen());
        } //TODO: Use String max-min length
        if (RandGenField.GeneratorValueType.DOUBLE.equals(ann.type())) {
            return decimalFormat.format(getDoubleGen().gen());
        }
        throw new GenRuntimeException("Not found generator with Long-result for annotation type "+ ann.type());
    }

    private ParserResult collectFields(Class c) {
        ParseByFields parser = new ParseByFields();
        ParserResult result = parser.parse(c);
        return result;
    }

    private List<Field> getFieldsFromParents(Class c) {
        List<Field> result = new LinkedList<>();
        result.addAll(Arrays.asList(c.getDeclaredFields()));

        Class temp = c;
        while (true) {
            Class parent = temp.getSuperclass();
            if (parent != null && !parent.getName().equals(Object.class.getName())) {
                result.addAll(Arrays.asList(parent.getDeclaredFields()));
                temp = parent;
            } else
                break;
        }
        return result;
    }

    private static final Object syncBooleanGenObj = new Object();

    private static BooleanGen getBooleanGen() {
        if (AnnotationsDataFill.booleanGenInstance != null) {
            return AnnotationsDataFill.booleanGenInstance;
        } else {
            synchronized(syncBooleanGenObj) {
                if (AnnotationsDataFill.booleanGenInstance != null)
                    return AnnotationsDataFill.booleanGenInstance;
                else {
                    BooleanGen gen = new BooleanGen();
                    AnnotationsDataFill.booleanGenInstance = gen;
                    return AnnotationsDataFill.booleanGenInstance;
                }
            }
        }
    }

    private static final Object syncIntegerGenObj = new Object();

    private static IntegerGen getIntegerGen() {
        if (AnnotationsDataFill.integerGenInstance != null) {
            return AnnotationsDataFill.integerGenInstance;
        } else {
            synchronized(syncIntegerGenObj) {
                if (AnnotationsDataFill.integerGenInstance != null)
                    return AnnotationsDataFill.integerGenInstance;
                else {
                    IntegerGen gen = new IntegerGen();
                    AnnotationsDataFill.integerGenInstance = gen;
                    return AnnotationsDataFill.integerGenInstance;
                }
            }
        }
    }

    private static final Object syncStringGenObj = new Object();

    private static StringGen getStringGen(RandGenField ann) {
        if (AnnotationsDataFill.stringGenInstance != null) {
            return AnnotationsDataFill.stringGenInstance;
        } else {
            synchronized(syncStringGenObj) {
                if (AnnotationsDataFill.stringGenInstance != null)
                    return AnnotationsDataFill.stringGenInstance;
                else {
                    int minLen = ann.stringMinLength();
                    int maxLen = ann.stringMaxLength();
                    StringGen gen = new StringGen(StringGen.StringGenType.RUS_WORD, minLen, maxLen);
                    AnnotationsDataFill.stringGenInstance = gen;
                    return AnnotationsDataFill.stringGenInstance;
                }
            }
        }
    }

    private static final Object syncDoubleGenObj = new Object();

    private static DoubleGen getDoubleGen() {
        if (AnnotationsDataFill.doubleGenInstance != null) {
            return AnnotationsDataFill.doubleGenInstance;
        } else {
            synchronized(syncDoubleGenObj) {
                if (AnnotationsDataFill.doubleGenInstance != null)
                    return AnnotationsDataFill.doubleGenInstance;
                else {
                    DoubleGen gen = new DoubleGen();
                    AnnotationsDataFill.doubleGenInstance = gen;
                    return AnnotationsDataFill.doubleGenInstance;
                }
            }
        }
    }

    public enum ScanType {
        BY_CLASS,
        BY_FIELDS
    }

}
