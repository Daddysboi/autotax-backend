package com.autotax.infrastructure.security.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Olaleye Afolabi <oafolabi@byteworks.com.ng>
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Localhost {
}
