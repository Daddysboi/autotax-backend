package com.autotax.infrastructure.configuration.security;

import com.bw.cfs.security.constraint.Scoped;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2020
 **/
@Deprecated
public class CustomPermissionAllowedMethodSecurityMetadataSource
        extends AbstractFallbackMethodSecurityMetadataSource {
    @Override
    protected Collection findAttributes(Class<?> clazz) {
        return null;
    }

    @Override
    protected Collection findAttributes(Method method, Class<?> targetClass) {
        if (!MergedAnnotations.from(targetClass).isPresent(RestController.class)) {
            return null;
        }
        if (hasAuthorityAnnotations(method)) {
            return null;
        }
        List attributes = new ArrayList<>();

//        attributes.add(DENY_ALL_ATTRIBUTE);

        return attributes;
    }

    private boolean hasAuthorityAnnotations(Method method) {
        MergedAnnotations mergedAnnotations = MergedAnnotations.from(method);
        return mergedAnnotations.isPresent(PreAuthorize.class)
                || mergedAnnotations.isPresent(PostAuthorize.class)
                || mergedAnnotations.isPresent(Scoped.class);
    }

    @Override
    public Collection getAllConfigAttributes() {
        return null;
    }
}
