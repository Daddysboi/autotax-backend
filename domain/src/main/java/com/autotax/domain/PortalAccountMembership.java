package com.autotax.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "portal_account_membership")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PortalAccountMembership extends StatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private PortalAccount portalAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private PortalUser portalUser;

    @Enumerated(EnumType.STRING)
    private GenericStatusConstant status;

    // You might want to add other fields specific to the membership here
    
}
