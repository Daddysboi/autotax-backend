package com.autotax.infrastructure.security.constraint;

import com.autotax.domain.PermissionTypeConstant;
import com.autotax.domain.PortalAccountTypeConstant;
import com.autotax.infrastructure.security.HasPermissionAccessManager;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@AccessConstraint(HasPermissionAccessManager.class)
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface HasPermission {


    PortalAccountTypeConstant accountType();

    PermissionTypeConstant[] value();


}
