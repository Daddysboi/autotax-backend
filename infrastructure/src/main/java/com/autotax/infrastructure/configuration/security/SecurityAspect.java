package com.autotax.infrastructure.configuration.security;

import com.bw.cfs.auth.Scope;
import com.bw.cfs.principal.RequestPrincipal;
import com.bw.cfs.security.constraint.Scoped;
import com.bw.integration.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2020
 **/
@Aspect
@Component
@Slf4j
public class SecurityAspect {
    @Inject
    private RequestPrincipal requestPrincipal;

    @Around("@annotation(com.bw.cfs.security.constraint.Scoped)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        if(!requestPrincipal.isAuthenticated()){
//            if(Objects.isNull(requestPrincipal.getOrganizationMember())){
//                throw new ErrorResponse(401, "Unauthorized");
//            }
//        }
        log.debug("====> security aspect intercepted request: authenticated [{}] :: scopes [{}]", requestPrincipal.isAuthenticated(), requestPrincipal.getScope());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Scoped scoped = method.getAnnotation(Scoped.class);
        if (requestPrincipal.hasAnyScope(Arrays.stream(scoped.value()).map(Scope::getCode).toArray(String[]::new))) {
            return joinPoint.proceed();
        }
        throw new ErrorResponse(403, "Forbidden access");
    }
}
