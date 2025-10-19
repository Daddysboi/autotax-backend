package com.autotax.infrastructure.configuration.security;

import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2020
 **/
//@Configuration
//@EnableGlobalMethodSecurity(
//        prePostEnabled = true,jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
//    @Override
//    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
//        return new CustomPermissionAllowedMethodSecurityMetadataSource();
//    }

//    @Override
//    protected MethodSecurityExpressionHandler createExpressionHandler() {
//        CustomMethodSecurityExpressionHandler expressionHandler =
//                new CustomMethodSecurityExpressionHandler();
////        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
//        return expressionHandler;
//    }

}
