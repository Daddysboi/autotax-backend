package com.autotax.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class NameCodeEntity extends StatusEntity {
    @Basic(
            optional = false
    )
    @Column(
            nullable = false
    )
    protected String name;
    @Basic(
            optional = false
    )
    @Column(
            unique = true,
            nullable = false
    )
    protected String code;
}
