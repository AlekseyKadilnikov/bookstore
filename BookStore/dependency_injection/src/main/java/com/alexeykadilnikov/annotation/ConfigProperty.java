package com.alexeykadilnikov.annotation;

import org.sonatype.guice.asm.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigProperty {
    String value() default "";
//    String configName() default "properties\\bookstore.yml";
//    String propertyName() default ElementType.FIELD.name().toString();
//    Type type() default Type.BYTE_TYPE;
}
