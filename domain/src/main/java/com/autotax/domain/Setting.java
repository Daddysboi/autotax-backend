package com.autotax.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Setting {
    @Id
    private Long id;
    private String name;
    private String value;

    @Basic
    private String description;
    @Basic
    private Boolean editable;
    @Basic
    private LocalDateTime createdAt;
}
