/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autotax.infrastructure.validator;


import com.autotax.dao.AppRepository;
import com.autotax.domain.constraint.ExistsColumnValue;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Olaleye Afolabi <oafolabi@byteworks.com.ng>
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExistsColumnValueValidator implements ExistsColumnValue.Validator {

    @Inject
    private AppRepository appRepository;
    private Class<?> entityType;
    private String columnName;

    @Override
    public void initialize(ExistsColumnValue constraintAnnotation) {
        this.entityType = constraintAnnotation.value();
        this.columnName = constraintAnnotation.columnName();
    }


    @Transactional
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return appRepository.findFirstByField(entityType, columnName, value).isPresent();
    }
}
