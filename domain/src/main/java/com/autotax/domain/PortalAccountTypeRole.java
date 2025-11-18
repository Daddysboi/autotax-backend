package com.autotax.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PortalAccountTypeRole {
    @Id
    private Long id;
    // Add other fields as needed
}