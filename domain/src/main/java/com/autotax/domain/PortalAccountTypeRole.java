package com.autotax.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portal_account_type_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PortalAccountTypeRole extends StatusEntity {

    @Column(name = "name", nullable = false, length = 1024)
    private String name;

    @Column(name = "display_name", nullable = false, length = 1024)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    public PortalAccountTypeConstant accountType;

    private Boolean playerRole;

    private Boolean systemDefined;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portal_account_id")
    private PortalAccount portalAccount;

    @OneToMany(mappedBy = "portalAccountTypeRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortalAccountMemberRole> portalAccountMemberRoles = new ArrayList<>();

    // Helper methods for managing the collection
    public void addPortalAccountMemberRole(PortalAccountMemberRole portalAccountMemberRole) {
        this.portalAccountMemberRoles.add(portalAccountMemberRole);
        portalAccountMemberRole.setPortalAccountTypeRole(this);
    }

    public void removePortalAccountMemberRole(PortalAccountMemberRole portalAccountMemberRole) {
        this.portalAccountMemberRoles.remove(portalAccountMemberRole);
        portalAccountMemberRole.setPortalAccountTypeRole(null);
    }
}
