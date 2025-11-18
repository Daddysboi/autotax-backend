package com.autotax.domain;


import jakarta.persistence.*;

@Entity
public class Lga extends NameCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private State state;
}
