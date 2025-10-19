package com.autotax.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Permission extends StatusEntity {

    @Column(name = "name", nullable = false, unique = true, length = 1024)
    private String name;

    @Column(name = "description", length = 2048)
    private String description;
}
