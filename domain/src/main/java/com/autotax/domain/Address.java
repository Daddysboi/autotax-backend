package com.autotax.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Address extends StatusEntity {

    private String line1;
    private String line2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}
