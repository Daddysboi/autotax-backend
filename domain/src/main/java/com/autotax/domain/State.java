package com.autotax.domain;

import jakarta.persistence.*;

@Entity
public class State extends NameCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private Country country;
}
