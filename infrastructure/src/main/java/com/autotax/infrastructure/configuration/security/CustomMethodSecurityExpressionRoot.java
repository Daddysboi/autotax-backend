package com.autotax.infrastructure.configuration.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2020
 **/
@Deprecated
public class CustomMethodSecurityExpressionRoot
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean hasScope(String scope) {
        return true;
    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setFilterObject(Object filterObject) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object returnObject) {

    }

    @Override
    public Object getThis() {
        return null;
    }
}
