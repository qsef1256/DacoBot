package net.qsef1256.dacobot.module.common.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: use this
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapEntity {

    Class<?> entity();

}