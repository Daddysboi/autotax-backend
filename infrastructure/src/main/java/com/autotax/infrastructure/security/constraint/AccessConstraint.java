package com.autotax.infrastructure.security.constraint;

import com.autotax.infrastructure.security.AccessStatusSource;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Neme Iloeje niloeje@byteworks.com.ng
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
@Documented
public @interface AccessConstraint {

    Class<? extends AccessStatusSource<?>> value();
}
