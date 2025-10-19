package com.autotax.infrastructure.security;

import java.lang.annotation.Annotation;

/**
 * @author Neme Iloeje niloeje@byteworks.com.ng
 */
public interface AccessStatusSource<A extends Annotation> {

    AccessStatus getStatus(A accessConstraint);
}
