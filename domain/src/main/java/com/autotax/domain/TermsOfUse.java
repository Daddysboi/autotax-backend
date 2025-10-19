package com.autotax.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "terms_of_use")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TermsOfUse extends StatusEntity {

    private String version;

    @Lob // Use @Lob for potentially long text fields
    @Column(name = "terms", nullable = false)
    private String terms;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portal_account_id", nullable = false) // Assuming a foreign key column named portal_account_id
    private PortalAccount portalAccount;
}
