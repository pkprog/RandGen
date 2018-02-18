package ru.pk.randgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.annotations.RandGenClass;
import ru.pk.randgen.annotations.RandGenField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AnnotationsDataFill {
    private final static Logger LOG = LoggerFactory.getLogger(AnnotationsDataFill.class);

    private static Class targetClassAnnotation = RandGenClass.class;
    private static String targetClassAnnotationName = RandGenClass.class.getName();
    private static Class targetFieldAnnotation = RandGenField.class;
    private static String targetFieldAnnotationName = RandGenField.class.getName();

    //init
    private static BooleanGen booleanGen = new BooleanGen();
    private static IntegerGen integerGen = new IntegerGen();

    private AnnotationsDataFill() {}

    //Поиск пометок аннотациями и заполнение данными
    @SuppressWarnings("unchecked")
    public static void fill(Object obj) {
        Class c = obj.getClass();
        Annotation foundClassAnn = c.getAnnotation(targetClassAnnotation);
        if (foundClassAnn == null) {
            LOG.debug("Not found class-annotation {} in class {}", targetClassAnnotation.getName(), obj.getClass().getName());
            return;
        }
        LOG.debug("Found class {} with annotation {}", obj.getClass().getName(), targetClassAnnotation.getName());

        List<Map<Field, Annotation>> goodFields = new LinkedList<>();
        Field[] declaredFields = c.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Annotation fieldAnn = declaredFields[i].getAnnotation(targetFieldAnnotation);
            if (fieldAnn != null) {
                LOG.debug("Found field {} annotation {}", declaredFields[i].getName(), targetFieldAnnotation.getName());
                if (checkField(declaredFields[i], fieldAnn))
                    goodFields.add(Collections.singletonMap(declaredFields[i], fieldAnn));
                else
                    LOG.debug("Field {} not added due checks failure", declaredFields[i].getName());
            }
        }

        LOG.debug("Correct fields with annotations {}", goodFields.size());
        for (Map<Field, Annotation> tempMap: goodFields) {
            Field f = tempMap.keySet().iterator().next();
            RandGenField a = (RandGenField) tempMap.get(f);
            LOG.debug("Processing field {}", f.getName());
            //
            Class typeOfField = f.getType();
            boolean accessible = f.isAccessible();
            if (typeOfField.isPrimitive()) {
                if (typeOfField.getName().equals("boolean") && a.type().equals(RandGenField.ClassType.BOOLEAN)) {
                    LOG.debug("Found {} primitive field {}", typeOfField.getName(), f.getName());
                    if (!accessible) f.setAccessible(true);
                    try {
                        f.setBoolean(obj, booleanGen.gen().booleanValue());
                    } catch (IllegalAccessException e) {
                        LOG.error("Illegal access to field {}. Error: {}", f.getName(), e.getMessage());
                        e.printStackTrace();
                    } finally {
                        f.setAccessible(accessible);
                    }
                } else
                if (typeOfField.getName().equals("int") && a.type().equals(RandGenField.ClassType.INTEGER)) {
                    LOG.debug("Found {} primitive field {}", typeOfField.getName(), f.getName());
                    if (!accessible) f.setAccessible(true);
                    try {
                        f.setInt(obj, integerGen.gen());
                    } catch (IllegalAccessException e) {
                        LOG.error("Illegal access to field {}. Error: {}", f.getName(), e.getMessage());
                        e.printStackTrace();
                    } finally {
                        f.setAccessible(accessible);
                    }
                } else //TODO: LongGen
                if (typeOfField.getName().equals("long") && a.type().equals(RandGenField.ClassType.LONG)) {
                    LOG.debug("Found {} primitive field {}", typeOfField.getName(), f.getName());
                    if (!accessible) f.setAccessible(true);
                    try {
                        f.setLong(obj, integerGen.gen());
                    } catch (IllegalAccessException e) {
                        LOG.error("Illegal access to field {}. Error: {}", f.getName(), e.getMessage());
                        e.printStackTrace();
                    } finally {
                        f.setAccessible(accessible);
                    }
                }
            } else {
                if (typeOfField.equals(Boolean.class) && a.type().equals(RandGenField.ClassType.BOOLEAN)) {
                    LOG.debug("Found {} object field {}", typeOfField.getSimpleName(), f.getName());
                    if (!accessible) f.setAccessible(true);
                    try {
                        f.set(obj, booleanGen.gen());
                    } catch (IllegalAccessException e) {
                        LOG.error("Illegal access to field {}. Error: {}", f.getName(), e.getMessage());
                        e.printStackTrace();
                    } finally {
                        f.setAccessible(accessible);
                    }
                } else
                if (typeOfField.equals(Integer.class) && a.type().equals(RandGenField.ClassType.INTEGER)) {
                    LOG.debug("Found {} object field {}", typeOfField.getSimpleName(), f.getName());
                    if (!accessible) f.setAccessible(true);
                    try {
                        f.set(obj, integerGen.gen());
                    } catch (IllegalAccessException e) {
                        LOG.error("Illegal access to field {}. Error: {}", f.getName(), e.getMessage());
                        e.printStackTrace();
                    } finally {
                        f.setAccessible(accessible);
                    }
                } else //TODO: LongGen
                if (typeOfField.equals(Long.class) && a.type().equals(RandGenField.ClassType.LONG)) {
                    LOG.debug("Found {} object field {}", typeOfField.getSimpleName(), f.getName());
                    if (!accessible) f.setAccessible(true);
                    try {
                        f.set(obj, integerGen.gen().longValue());
                    } catch (IllegalAccessException e) {
                        LOG.error("Illegal access to field {}. Error: {}", f.getName(), e.getMessage());
                        e.printStackTrace();
                    } finally {
                        f.setAccessible(accessible);
                    }
                }
            }
        }

    }

    private static boolean checkField(Field field, Annotation ann) {
        return true;
    }
}
