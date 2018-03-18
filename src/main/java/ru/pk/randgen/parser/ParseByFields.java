package ru.pk.randgen.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.annotations.RandGenClass;
import ru.pk.randgen.annotations.RandGenField;
import ru.pk.randgen.exceptions.GenRuntimeException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ParseByFields implements Parser {
    private final static Logger LOG = LoggerFactory.getLogger(ParseByFields.class);

    @Override
    public ParserResult parse(Class targetClass) {
        final String className = targetClass.getName();
        RandGenClass foundClassAnn = (RandGenClass) targetClass.getAnnotation(targetClassAnnotation);
        if (foundClassAnn == null) {
            LOG.debug("Not found class-annotation {} in class {}", targetClassAnnotationName, className);
            throw new GenRuntimeException("Not found class-annotation "+ targetClassAnnotationName +" in class " + className);
        }
        LOG.trace("Found class {} with annotation {}", className, targetClassAnnotationName);

        boolean withParent = RandGenClass.IncludeSuperclass.YES.equals(foundClassAnn.withParent());
        ParsedFields getFieldsResult = collectFields(targetClass, withParent);

        return new ParserResult(targetClass, getFieldsResult.goodFields, getFieldsResult.badFields);
    }

    private ParsedFields collectFields(Class c, boolean withParent) {
        ParsedFields result = new ParsedFields();

        List<Field> declaredFields;

        if (withParent) {
            declaredFields = getFieldsFromParents(c);
        } else {
            Field[] declaredFieldsArray = c.getDeclaredFields();
            declaredFields = Arrays.asList(declaredFieldsArray);
        }

        for (int i = 0; i < declaredFields.size(); i++) {
            Field f = declaredFields.get(i);
            RandGenField fieldAnn = (RandGenField) f.getAnnotation(targetFieldAnnotation);
            if (fieldAnn != null) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Found field {} annotated by {}", f.getName(), targetFieldAnnotationName);
                }
                if (CompatChecker.check(f, fieldAnn))
                    result.goodFields.add(new FieldAnnotation(f, fieldAnn));
                else {
                    result.badFields.add(new FieldAnnotation(f, fieldAnn));
                }
            }
        }

        LOG.debug("Correct fields with annotations count={}", result.goodFields.size());
        LOG.debug("Incorrect fields with annotations count={}", result.badFields.size());

        return result;
    }

    private List<Field> getFieldsFromParents(Class c) {
        List<Field> result = new LinkedList<>();
        result.addAll(Arrays.asList(c.getDeclaredFields()));

        final String CLASS_NAME_OBJECT = Object.class.getName();
        Class temp = c;
        while (true) {
            Class parent = temp.getSuperclass();
            if (parent != null && !parent.getName().equals(CLASS_NAME_OBJECT)) {
                result.addAll(Arrays.asList(parent.getDeclaredFields()));
                temp = parent;
            } else
                break;
        }
        return result;
    }

    class ParsedFields {
        List<FieldAnnotation> goodFields = new LinkedList<>();
        List<FieldAnnotation> badFields = new LinkedList<>();
    }

}
