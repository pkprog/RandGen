package ru.pk.randgen.annotations;

import java.lang.annotation.*;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RandGenField {
    ClassType type();
    boolean defaultNull() default false;
    String rangeStart() default "";
    String rangeEnd() default "";

    public enum ClassType {
        BOOLEAN,
        STRING,
        INTEGER,
        LONG,
        DOUBLE
    }

}
