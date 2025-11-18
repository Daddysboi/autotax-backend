package com.autotax.domain;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Country extends StatusEntity {
    @Basic(
            optional = false
    )
    @Column(
            unique = true,
            nullable = false
    )
    public String name;
    @Basic(
            optional = false
    )
    @Column(
            unique = true,
            nullable = false
    )
    private String alpha2;
    @Basic(
            optional = false
    )
    @Column(
            unique = true,
            nullable = false
    )
    private String alpha3;
    @Basic
    private String internationalDialingCode;
}
