package com.autotax.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "portal_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PortalAccount extends StatusEntity {

    @Column(name = "name", nullable = false, length = 1024)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PortalAccountTypeConstant type;

    @Column(name = "code", nullable = false, length = 1024)
    private String code;

    @Column(unique = true)
    private String domain;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id") // Assuming a foreign key column named address_id
    private Address address;
}
