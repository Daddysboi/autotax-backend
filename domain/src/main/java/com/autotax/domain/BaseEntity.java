package com.autotax.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(
            generator = "id",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "id"
    )
    protected Long id;
    @Basic
    @Column(
            nullable = false
    )
    protected LocalDateTime createdAt;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private PortalUser createdBy;
}
