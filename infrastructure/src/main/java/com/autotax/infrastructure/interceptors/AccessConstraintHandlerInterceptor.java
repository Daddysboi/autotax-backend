/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autotax.infrastructure.interceptors;

import com.autotax.domain.principal.RequestPrincipal;
import com.autotax.infrastructure.security.AccessStatus;
import com.autotax.infrastructure.security.AccessStatusSource;
import com.autotax.infrastructure.security.constraint.AccessConstraint;
import com.autotax.infrastructure.security.constraint.Scoped;
import com.autotax.integration.apiclient.ApiResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.webmvc.api.MultipleOpenApiResource;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springdoc.webmvc.ui.SwaggerWelcomeWebMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Temitope temitopeahmedyusuf@gmail.com
 */
public class AccessConstraintHandlerInterceptor implements HandlerInterceptor {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ApplicationContext applicationContext;
    private final List<Class<?>> whiteListTypes = new ArrayList<>();

    @Autowired
    private RequestPrincipal requestPrincipal;
    @Autowired
    private Gson gson;

    public AccessConstraintHandlerInterceptor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        whiteListTypes.add(BasicErrorController.class);
        whiteListTypes.add(OpenApiResource.class);
        whiteListTypes.add(MultipleOpenApiResource.class);
        whiteListTypes.add(SwaggerWelcomeWebMvc.class);
        whiteListTypes.add(BasicErrorController.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        try {
            ApiResponse<String> apiResponse = new ApiResponse<>();
            apiResponse.setCode(401);
            apiResponse.setMessage("Unauthorized");
            List<Annotation> accessConstraints = collectAccessConstraints(handlerMethod.getMethod().getDeclaringClass().getAnnotations());
            accessConstraints.addAll(collectAccessConstraints(handlerMethod.getMethod().getDeclaredAnnotations()));

            if (!requestPrincipal.isAuthenticated()) {
                if (accessConstraints.isEmpty() && (
                        hasAuthorityAnnotation(handlerMethod.getMethod())
                                || whiteListTypes.stream().anyMatch(aClass -> aClass.isAssignableFrom(handlerMethod.getBeanType()))
                )) {
                    return true;
                }
                response.setStatus(401);
                response.getWriter().append(gson.toJson(apiResponse));
                return false;
            }

            for (Annotation annotation : accessConstraints) {
                AccessStatus accessStatus = getAccessStatus(annotation);
                if (!accessStatus.hasAccess()) {
                    response.setStatus(403);
                    apiResponse.setMessage(accessStatus.reason());
                    apiResponse.setCode(403);
                    response.getWriter().append(gson.toJson(apiResponse));
                    return false;
                }
            }

            return true;
        } catch (IllegalStateException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    private boolean hasAuthorityAnnotation(Method method) {
        MergedAnnotations mergedAnnotations = MergedAnnotations.from(method);
        return mergedAnnotations.isPresent(PreAuthorize.class)
                || mergedAnnotations.isPresent(PostAuthorize.class)
                || mergedAnnotations.isPresent(Scoped.class);
    }

    private List<Annotation> collectAccessConstraints(Annotation[] stream) {
        return Arrays.asList(stream).stream().filter(annotation -> annotation.annotationType().isAnnotationPresent(AccessConstraint.class)).collect(Collectors.toList());
    }

    private <A extends Annotation> AccessStatus getAccessStatus(A annotation) {
        Class<? extends AccessStatusSource<A>> aClass = (Class<AccessStatusSource<A>>) annotation.annotationType().getAnnotation(AccessConstraint.class).value();
        AccessStatusSource<A> accessStatusSource = applicationContext.getBean(aClass);
        if (accessStatusSource == null) {
            logger.error("No bean of type: {}", aClass);
        }
        return accessStatusSource.getStatus(annotation);
    }
}
