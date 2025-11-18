package com.autotax.infrastructure.etc;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.SequenceGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigInteger;

public abstract class SequenceGeneratorImpl implements SequenceGenerator {
    private final EntityManager entityManager;
    private final String sequenceName;
    private final TransactionTemplate transactionTemplate;
    @Value("${SEQUENCE_DEFINITION_SQL_QUERY}")
    private String sequenceDefinition;

    public SequenceGeneratorImpl(EntityManager entityManager, TransactionTemplate transactionTemplate, String sequenceTableName) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
        this.sequenceName = sequenceTableName.toLowerCase() + "_sequence";
    }

    @PostConstruct
    public void init() {
        transactionTemplate.execute(tx -> {
            this.entityManager.createNativeQuery(String.format(sequenceDefinition, sequenceName))
                    .executeUpdate();
            return null;
        });
    }

}
