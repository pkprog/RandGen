package ru.pk.randgen.fillertypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.AnnotationsDataFill;
import ru.pk.randgen.annotations.RandGenClass;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.exceptions.GenRuntimeException;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class ByFields {
    private final static Logger LOG = LoggerFactory.getLogger(ByFields.class);

    private Class targetClass;


    public ByFields(Class targetClass) {
        this.targetClass = targetClass;
    }

    public void process(Object obj) {
        if (!targetClass.equals(obj.getClass())) {
            throw new GenRuntimeException("Object.class <> targetClass");
        }
        RandGenClass foundClassAnn = (RandGenClass) c.getAnnotation(targetClassAnnotation);
        if (foundClassAnn == null) {
            LOG.debug("Not found class-annotation {} in class {}", targetClassAnnotationName, obj.getClass().getName());
            return;
        }
        LOG.debug("Found class {} with annotation {}", obj.getClass().getName(), targetClassAnnotationName);
        boolean withParent = foundClassAnn.withParent().equals(RandGenClass.IncludeSuperclass.YES);
        GetFieldsResult getFieldsResult = collectFields(this.targetClass, withParent);

        fillByFields(obj, getFieldsResult.goodFields);
    }


    private GetFieldsResult collectFields(Class c, boolean withParent) {
        GetFieldsResult result = new GetFieldsResult();

        if (withParent) {
            List<Field> declaredFields = getFieldsFromParents(c);
            for (int i = 0; i < declaredFields.size(); i++) {
                Field f = declaredFields.get(i);
                RandGenField fieldAnn = (RandGenField) f.getAnnotation(targetFieldAnnotation);
                if (fieldAnn != null) {
                    LOG.debug("Found field {} annotation {}", f.getName(), targetFieldAnnotationName);
                    if (checkFieldType(f, fieldAnn))
                        result.goodFields.add(new FieldAnnotation(f, fieldAnn));
                    else {
                        LOG.debug("Field {} not added due checks failure", f.getName());
                        result.badFields.add(new FieldAnnotation(f, fieldAnn));
                    }
                }
            }
        } else {
            Field[] declaredFields = c.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field f = declaredFields[i];
                RandGenField fieldAnn = (RandGenField) f.getAnnotation(targetFieldAnnotation);
                if (fieldAnn != null) {
                    LOG.debug("Found field {} annotation {}", f.getName(), targetFieldAnnotationName);
                    if (checkFieldType(f, fieldAnn))
                        result.goodFields.add(new FieldAnnotation(f, fieldAnn));
                    else {
                        LOG.debug("Field {} not added due checks failure", f.getName());
                        result.badFields.add(new FieldAnnotation(f, fieldAnn));
                    }
                }
            }
        }

        LOG.debug("Correct fields with annotations count={}", result.goodFields.size());
        LOG.debug("Incorrect fields with annotations count={}", result.badFields.size());

        return result;
    }

    private void fillByFields(Object obj, List<AnnotationsDataFill.FieldAnnotation> goodFields) {
        for (AnnotationsDataFill.FieldAnnotation fa: goodFields) {
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
                    if (typeOfField.getName().equals("int")) {
                        if (!accessible) f.setAccessible(true);
                        f.setInt(obj, generateInteger(ann));
                    } else //TODO: LongGen
                        if (typeOfField.getName().equals("long")) {
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
                    if (typeOfField.equals(Boolean.class)) {
                        if (!accessible) f.setAccessible(true);
                        f.set(obj, generateBoolean(ann));
                    } else
                    if (typeOfField.equals(Integer.class)) {
                        if (!accessible) f.setAccessible(true);
                        f.set(obj, generateInteger(ann));
                    } else //TODO: LongGen
                        if (typeOfField.equals(Long.class)) {
                            if (!accessible) f.setAccessible(true);
                            f.set(obj, generateLong(ann));
                        } else
                        if (typeOfField.equals(String.class)) {
                            if (!accessible) f.setAccessible(true);
                            f.set(obj, generateString(ann));
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


    private boolean checkFieldType(Field field, RandGenField ann) {
        Class typeOfField = field.getType();

        if (typeOfField.isPrimitive()) {
            //primitive
            switch (typeOfField.getName()) {
                case "boolean":
                    return ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN) ||
                            ann.type().equals(RandGenField.GeneratorValueType.INTEGER) ||
                            ann.type().equals(RandGenField.GeneratorValueType.LONG) ||
                            ann.type().equals(RandGenField.GeneratorValueType.STRING) ||
                            ann.type().equals(RandGenField.GeneratorValueType.DOUBLE);
                case "int":
                    return ann.type().equals(RandGenField.GeneratorValueType.INTEGER) ||
                            ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN) ||
                            ann.type().equals(RandGenField.GeneratorValueType.LONG);
                case "long":
                    return ann.type().equals(RandGenField.GeneratorValueType.LONG) ||
                            ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN) ||
                            ann.type().equals(RandGenField.GeneratorValueType.INTEGER);
            }

            return false;
        }

        //Object
        if (typeOfField.equals(Long.class)) {
            return ann.type().equals(RandGenField.GeneratorValueType.LONG) ||
                    ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN) ||
                    ann.type().equals(RandGenField.GeneratorValueType.INTEGER);
        }
        if (typeOfField.equals(Integer.class)) {
            return ann.type().equals(RandGenField.GeneratorValueType.INTEGER) ||
                    ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN) ||
                    ann.type().equals(RandGenField.GeneratorValueType.LONG);
        }
        if (typeOfField.equals(Boolean.class)) {
            return ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN) ||
                    ann.type().equals(RandGenField.GeneratorValueType.INTEGER) ||
                    ann.type().equals(RandGenField.GeneratorValueType.LONG) ||
                    ann.type().equals(RandGenField.GeneratorValueType.STRING) ||
                    ann.type().equals(RandGenField.GeneratorValueType.DOUBLE);
        }
        if (typeOfField.equals(String.class)) {
            return ann.type().equals(RandGenField.GeneratorValueType.BOOLEAN) ||
                    ann.type().equals(RandGenField.GeneratorValueType.INTEGER) ||
                    ann.type().equals(RandGenField.GeneratorValueType.LONG) ||
                    ann.type().equals(RandGenField.GeneratorValueType.STRING) ||
                    ann.type().equals(RandGenField.GeneratorValueType.DOUBLE);
        }

        return false;
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
