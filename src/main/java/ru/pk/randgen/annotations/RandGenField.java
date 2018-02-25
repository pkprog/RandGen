package ru.pk.randgen.annotations;

import java.lang.annotation.*;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RandGenField {
    GeneratorValueType type();
    boolean defaultNull() default false;
    String rangeStart() default "";
    String rangeEnd() default "";
    int stringMinLength() default 1;
    int stringMaxLength() default 10;

    public enum GeneratorValueType {
        BOOLEAN,
        STRING,
        INTEGER,
        LONG,
        DOUBLE
    }

}
