package com.autotax.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "portal_account_member_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PortalAccountMemberRole extends StatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private PortalAccountTypeRole portalAccountTypeRole;

    @ManyToOne(fetch = FetchType.LAZY)
    private PortalUser portalUser;

    // You might want to add other fields specific to a member's role here
    private String roleName; // Example field
}
