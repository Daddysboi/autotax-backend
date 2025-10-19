package com.autotax.infrastructure;

import org.hibernate.boot.model.FunctionContributions;//hibernate core library
import org.hibernate.boot.model.FunctionContributor;

public class PostgreSQL9DialectExtension implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry().registerPattern(
                "dayofyear", "extract(doy from ?1)"
        );
        functionContributions.getFunctionRegistry().registerPattern(
                "dayofweek", "extract(dow from ?1)"
        );
    }
}
