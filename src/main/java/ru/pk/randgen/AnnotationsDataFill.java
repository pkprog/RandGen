package ru.pk.randgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.annotations.RandGenClass;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.exceptions.GenRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class AnnotationsDataFill {
    private final static Logger LOG = LoggerFactory.getLogger(AnnotationsDataFill.class);

    private static Class targetClassAnnotation = RandGenClass.class;
    private static String targetClassAnnotationName;
    private static Class targetFieldAnnotation = RandGenField.class;
    private static String targetFieldAnnotationName;

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
        Class c = obj.getClass();
        Annotation foundClassAnn = c.getAnnotation(targetClassAnnotation);
        if (foundClassAnn == null) {
            LOG.debug("Not found class-annotation {} in class {}", targetClassAnnotationName, obj.getClass().getName());
            return;
        }
        LOG.debug("Found class {} with annotation {}", obj.getClass().getName(), targetClassAnnotationName);

        if (scanType.equals(ScanType.BY_CLASS)) {
            //По классу. Не глядя на аннотирование полей
            fillByClass();
        } else if (scanType.equals(ScanType.BY_FIELDS)) {
            //По аннотированным полям. Класс аннотировать тоже нужно.
            fillByFields(obj, c);
        }
    }

    private void fillByClass() {

    }

    private void fillByFields(Object obj, Class c) {
        GetFieldsResult getFieldsResult = getFields(c);
        List<FieldAnnotation> goodFields = getFieldsResult.goodFields;

        for (FieldAnnotation fa: goodFields) {
            Field f = fa.field;
            RandGenField ann = fa.annotation;
            LOG.debug("Processing field {}", f.getName());
            //
            Class typeOfField = f.getType();
            boolean accessible = f.isAccessible();
            if (typeOfField.isPrimitive()) {
                LOG.debug("Found {} primitive field {}", typeOfField.getName(), f.getName());
                try {
                    if (typeOfField.getName().equals("boolean")) {
                        if (!accessible) f.setAccessible(true);
                        f.setBoolean(obj, generateBoolean(ann));
                    } else
                    if (typeOfField.getName().equals("int") && ann.type().equals(RandGenField.GeneratorValueType.INTEGER)) {
                        if (!accessible) f.setAccessible(true);
                        f.setInt(obj, generateInteger(ann));
                    } else //TODO: LongGen
                    if (typeOfField.getName().equals("long") && ann.type().equals(RandGenField.GeneratorValueType.LONG)) {
                        if (!accessible) f.setAccessible(true);
                        f.setLong(obj, generateLong(ann));
                    }
                } catch (IllegalAccessException e) {
                    LOG.error("Illegal access to primitive field {}. Error: {}", f.getName(), e.getMessage());
                    e.printStackTrace();
                } finally {
                    f.setAccessible(accessible);
                }

            } else {
                LOG.debug("Found {} object field {}", typeOfField.getSimpleName(), f.getName());
                try {
                    if (typeOfField.equals(Boolean.class) && ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN)) {
                        if (!accessible) f.setAccessible(true);
                        f.set(obj, generateBoolean(ann));
                    } else
                    if (typeOfField.equals(Integer.class) && ann.type().equals(RandGenField.GeneratorValueType.INTEGER)) {
                        if (!accessible) f.setAccessible(true);
                        f.set(obj, generateInteger(ann));
                    } else //TODO: LongGen
                        if (typeOfField.equals(Long.class) && ann.type().equals(RandGenField.GeneratorValueType.LONG)) {
                            if (!accessible) f.setAccessible(true);
                            f.set(obj, generateLong(ann));
                        }
                } catch (IllegalAccessException e) {
                    LOG.error("Illegal access to object field {}. Error: {}", f.getName(), e.getMessage());
                    e.printStackTrace();
                } finally {
                    f.setAccessible(accessible);
                }
            }
        }
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

    private GetFieldsResult getFields(Class c) {
        GetFieldsResult result = new GetFieldsResult();
        //
        Field[] declaredFields = c.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            RandGenField fieldAnn = (RandGenField) declaredFields[i].getAnnotation(targetFieldAnnotation);
            if (fieldAnn != null) {
                LOG.debug("Found field {} annotation {}", declaredFields[i].getName(), targetFieldAnnotationName);
                if (checkFieldType(declaredFields[i], fieldAnn))
                    result.goodFields.add(new FieldAnnotation(declaredFields[i],  fieldAnn));
                else {
                    LOG.debug("Field {} not added due checks failure", declaredFields[i].getName());
                    result.badFields.add(new FieldAnnotation(declaredFields[i], fieldAnn));
                }
            }
        }

        LOG.debug("Correct fields with annotations count={}", result.goodFields.size());
        LOG.debug("Incorrect fields with annotations count={}", result.badFields.size());

        return result;
    }

    private boolean checkFieldType(Field field, RandGenField ann) {
        Class typeOfField = field.getType();

        if (typeOfField.isPrimitive()) {
            //primitive
            switch (typeOfField.getName()) {
                case "boolean":
                    return ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN);
                case "int":
                    return ann.type().equals(RandGenField.GeneratorValueType.INTEGER);
                case "long":
                    return ann.type().equals(RandGenField.GeneratorValueType.LONG);
            }

            return false;
        }

        //Object
        if (typeOfField.equals(Long.class)) {
            return ann.type().equals(RandGenField.GeneratorValueType.LONG);
        }
        if (typeOfField.equals(Integer.class)) {
            return ann.type().equals(RandGenField.GeneratorValueType.INTEGER);
        }
        if (typeOfField.equals(Boolean.class)) {
            return ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN);
        }

        return false;
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

    private static class GetFieldsResult {
        public List<FieldAnnotation> goodFields = new LinkedList<>();
        public List<FieldAnnotation> badFields = new LinkedList<>();
    }

    private static class FieldAnnotation {
        public Field field;
        public RandGenField annotation;

        public FieldAnnotation(Field field, RandGenField annotation) {
            this.field = field;
            this.annotation = annotation;
        }
    }
}
