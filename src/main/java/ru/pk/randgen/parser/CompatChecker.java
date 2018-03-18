package ru.pk.randgen.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.annotations.RandGenField;

import java.lang.reflect.Field;

public class CompatChecker {
    private final static Logger LOG = LoggerFactory.getLogger(CompatChecker.class);

    public static boolean check(Field field, RandGenField ann) {
        Class typeOfField = field.getType();
        final String fieldName = field.getName();

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

            LOG.trace("Field {} not compartible", fieldName);
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

        LOG.trace("Field {} not compartible", fieldName);
        return false;
    }

}
