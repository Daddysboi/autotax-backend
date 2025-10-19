package com.autotax.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class StatusEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GenericStatusConstant status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastModified = LocalDateTime.now();
        if (status == null) { // Set default status if not already set
            status = GenericStatusConstant.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = LocalDateTime.now();
    }
}
