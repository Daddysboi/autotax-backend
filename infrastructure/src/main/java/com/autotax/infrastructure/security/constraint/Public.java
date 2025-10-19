package com.autotax.infrastructure.security.constraint;

import org.springframework.security.access.prepost.PreAuthorize;

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
@PreAuthorize("isAnonymous() or isAuthenticated() ")
public @interface Public {
}
