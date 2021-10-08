package ru.pk.randgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.parser.FieldAnnotation;

import java.lang.reflect.Field;
import java.util.Collection;

public class Filler {
    private final static Logger LOG = LoggerFactory.getLogger(Filler.class);

    private ValueFactory valueFactory;

    public Filler() {
    }

    public void fill(Object obj, Collection<FieldAnnotation> fieldsAnnotations) {
        for (FieldAnnotation fa: fieldsAnnotations) {
            Field f = fa.field;
            RandGenField ann = fa.annotation;
            final String fieldName = f.getName();
            LOG.trace("Filling field {}", fieldName);

            Class typeOfField = f.getType();
            if (typeOfField.isPrimitive()) {
                if (LOG.isTraceEnabled())
                    LOG.trace("Fill primitive {} field {}", typeOfField.getName(), fieldName);
                doPrimitiveField(obj, f);
            } else {
                if (LOG.isTraceEnabled())
                    LOG.trace("Fill object {} field {}", typeOfField.getSimpleName(), fieldName);
                doObjectField(obj, f);
            }
        }
    }

    private void doPrimitiveField(Object obj, Field f) {
        final String fieldName = f.getName();
        Class typeOfField = f.getType();
        boolean accessible = f.isAccessible();

        if (!accessible) {
            try {
                f.setAccessible(true);
            } catch (SecurityException e) {
                LOG.error("Field {} not accessible! Continue without filling. Error: {}", fieldName, e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        try {
            if (typeOfField.getName().equals("boolean")) {
                f.setBoolean(obj, generateBoolean(ann));
            } else
            if (typeOfField.getName().equals("int")) {
                f.setInt(obj, generateInteger(ann));
            } else //TODO: LongGen
                if (typeOfField.getName().equals("long")) {
                    f.setLong(obj, generateLong(ann));
                }
        } catch (IllegalAccessException e) {
            LOG.error("Illegal access to primitive field {}. Error: {}", fieldName, e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                f.setAccessible(accessible);
            } catch (SecurityException e) {
                LOG.error("Field {} not accessible! Can not restore accessibility state. Continue restoring. Error: {}", fieldName, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void doObjectField(Object obj, Field f) {
        final String fieldName = f.getName();
        Class typeOfField = f.getType();
        boolean accessible = f.isAccessible();

        if (!accessible) {
            try {
                f.setAccessible(true);
            } catch (SecurityException e) {
                LOG.error("Field {} not accessible! Continue without filling. Error: {}", fieldName, e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        try {
            if (typeOfField.equals(Boolean.class)) {
                f.set(obj, generateBoolean(ann));
            } else
            if (typeOfField.equals(Integer.class)) {
                f.set(obj, generateInteger(ann));
            } else //TODO: LongGen
                if (typeOfField.equals(Long.class)) {
                    f.set(obj, generateLong(ann));
                } else
                if (typeOfField.equals(String.class)) {
                    f.set(obj, generateString(ann));
                }
        } catch (IllegalAccessException e) {
            LOG.error("Illegal access to object field {}. Error: {}", fieldName, e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                f.setAccessible(accessible);
            } catch (SecurityException e) {
                LOG.error("Field {} not accessible! Can not restore accessibility state. Continue restoring. Error: {}", fieldName, e.getMessage());
                e.printStackTrace();
            }
        }
    }



}
