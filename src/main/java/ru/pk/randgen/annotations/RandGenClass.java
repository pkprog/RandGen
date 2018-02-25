package ru.pk.randgen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RandGenClass {
    IncludeSuperclass withParent() default IncludeSuperclass.NO;

    public enum IncludeSuperclass {
        NO,
        YES
    }
}
